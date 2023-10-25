package com.example.fitnessrecord.global.mail;

import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailComponents {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendMailForRegister(SendMailDto sendMailDto){
        String mail = sendMailDto.getEmail();
        String subject = "[" + sendMailDto.getNickname()+ "]님 FitnessRecord 가입을 환영합니다. ";
        String text = new StringBuilder()
                .append("<p>FitnessRecord 가입을 환영합니다. </p>")
                .append("<p>아래 링크를 클릭하셔서 가입을 완료 하세요. </p>")
                .append("<div><a target='_blank' href='http://localhost:8080/auth/email-auth?key=" + sendMailDto.getAuthKey() + "'>이메일 인증하기</a></div>")
                .toString();

        sendMail(mail, subject, text);
        log.info("메일 전송 완료 - {}", mail);
    }

    private void sendMail(String mail, String subject, String text){
        boolean result = false;

        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(mail);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(text, true);
            }
        };

        try{
            javaMailSender.send(msg);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }


}
