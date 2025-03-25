package com.smartspending.user.service;

public interface MailService {

    void sendVerificationEmail(String toEmail, String code);

    String verifyCode();
}
