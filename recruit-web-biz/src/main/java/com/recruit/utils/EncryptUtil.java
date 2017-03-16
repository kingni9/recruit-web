package com.recruit.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhuangjt on 2017/3/16.
 */
public class EncryptUtil {
    private static final String DEFAULT_ENCRYPT_TYPE = "MD5";

    private static final String DEFAULT_CHARSET = "UTF8";

    /**
     * 将指定byte数组转换成16进制字符串
     * @param bytes
     * @return
     */
    private static String byteToHexString(byte[] bytes) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }

        return hexString.toString();
    }

    /**
     * 获得加密后的十六进制形式口令
     * @param key
     * @return
     */
    public static String getEncryptedStr(String key, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] saltBytes = salt.getBytes();
        MessageDigest md = MessageDigest.getInstance(DEFAULT_ENCRYPT_TYPE);
        md.update(saltBytes);
        md.update(key.getBytes(DEFAULT_CHARSET));
        byte[] digest = md.digest();

        return byteToHexString(digest);
    }
}
