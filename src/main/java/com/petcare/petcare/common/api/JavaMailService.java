package com.petcare.petcare.common.api;

import java.util.Properties;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JavaMailService {

    @Value("${spring.mail.username}")
    private String from;
    
    private final JavaMailSender javaMailSender;

    public void send(String to, String subject, String htmlBody) throws MessagingException {
        if (javaMailSender instanceof JavaMailSenderImpl) {
            Properties props = ((JavaMailSenderImpl) javaMailSender).getJavaMailProperties();
            props.put("mail.smtp.localhost", "127.0.0.1");
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        javaMailSender.send(message);
    }

    public void sendVerifyCodeEmail(String to, String code) throws MessagingException {
        String subject = "[PetCare] 이메일 인증번호 안내";
        String body = ""
            + "<div style='max-width:480px; margin:0 auto; padding:32px; "
            + "font-family:\"Pretendard\",\"Apple SD Gothic Neo\",sans-serif;'>"
            + "  <h2 style='color:#FD8B00; margin-bottom:8px;'>PetCare 이메일 인증</h2>"
            + "  <p style='color:#555; font-size:15px;'>아래 인증번호를 입력해 주세요.</p>"
            + "  <div style='background:#FFF8EE; border:2px solid #FD8B00; border-radius:12px; "
            + "  padding:24px; text-align:center; margin:24px 0;'>"
            + "    <span style='font-size:32px; font-weight:700; letter-spacing:8px; color:#333;'>"
            + code
            + "    </span>"
            + "  </div>"
            + "  <p style='color:#999; font-size:13px;'>인증번호는 5분간 유효합니다.</p>"
            + "</div>";

        send(to, subject, body);
    }

    public String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);  // 100000 ~ 999999
        return String.valueOf(code);
    }
}
