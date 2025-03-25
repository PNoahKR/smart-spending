package com.smartspending.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String toEmail, String code) {
        String subject = "[슬기로운 소비생활] 인증 코드";
        String htmlContent = "<html>" +
                "<body>" +
                "<h1>[슬기로운 소비생활] 입니다!</h1>" +
                "<p>저희 서비스를 이용해 주셔서 감사합니다. 이메일 인증코드는:</p>" +
                "<h2 style='color:blue;'>" + code + "</h2>" +
                "<p>인증 코드를 입력해 회원가입을 완료해주세요.</p>" +
                "<p>이 이메일을 요청하지 않으셨다면 무시해주시길 바랍니다.</p>" +
                "<hr>" +
                "<p>감사합니다.</p>" +
                "<p><strong>슬기로운 소비생활</strong></p>" +
                "</body>" +
                "</html>";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public String verifyCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer code = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }
}
