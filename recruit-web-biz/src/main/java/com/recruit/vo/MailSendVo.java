package com.recruit.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.PasswordAuthentication;
import java.io.Serializable;

/**
 * Created by zhuangjt on 2017/3/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailSendVo implements Serializable {
    private static final long serialVersionUID = -2961849466157193319L;

    private PasswordAuthentication authentication;

    private String mailTo;

    private String mailFrom;

    private String mailSubject;

    private String mailContent;
}
