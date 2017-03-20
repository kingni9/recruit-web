package com.recruit.utils;

import com.recruit.base.ResultDTO;
import com.recruit.vo.MailSendVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by zhuangjt on 2017/3/17.
 */
@Slf4j
public class MailUtil {
    private static final String DEFAULT_MAIL_SMTP_HOST_KEY = "mail.smtp.host";

    private static final String DEFAULT_MAIL_SMTP_AUTH_KEY = "mail.smtp.auth";

    private static final String DEFAULT_MAIL_SMTP_HOST_VALUE = "smtp.aliyun.com";

    private static final String DEFAULT_MAIL_SMTP_AUTH_VALUE = "true";

    public static ResultDTO<Boolean> sendMail(Properties properties, MailSendVo vo) {
        if(!validateArgus(vo)) {
            return ResultDTO.failed("illegal arguments");
        }

        if(properties == null) {
            properties = new Properties();
            properties.setProperty(DEFAULT_MAIL_SMTP_HOST_KEY, DEFAULT_MAIL_SMTP_HOST_VALUE);
            properties.setProperty(DEFAULT_MAIL_SMTP_AUTH_KEY, DEFAULT_MAIL_SMTP_AUTH_VALUE);
        }

        try {
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return vo.getAuthentication();
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(vo.getMailFrom()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(vo.getMailTo()));
            message.setSubject(vo.getMailSubject());
            message.setText(vo.getMailContent());
            Transport.send(message);

            return ResultDTO.succeed(Boolean.TRUE);
        } catch (Exception e) {
            log.error("failed to sendMail", e);
        }

        return ResultDTO.failed("error");
    }


    private static Boolean validateArgus(MailSendVo vo) {
        return vo != null
                && vo.getAuthentication() != null
                && StringUtils.isNoneBlank(vo.getAuthentication().getUserName())
                && StringUtils.isNoneBlank(vo.getAuthentication().getPassword())
                && StringUtils.isNoneBlank(vo.getMailFrom())
                && StringUtils.isNoneBlank(vo.getMailTo())
                && StringUtils.isNoneBlank(vo.getMailSubject())
                && StringUtils.isNoneBlank(vo.getMailContent());
    }

    public static void main(String[] args) {
        MailUtil.sendMail(null, MailSendVo.builder()
        .authentication(new PasswordAuthentication("zhuangjt@aliyun.com", ""))
        .mailFrom("zhuangjt@aliyun.com")
        .mailTo("522598027@qq.com")
        .mailSubject("Job Report")
        .mailContent("Job Reporting!")
        .build());
    }
}
