package com.github.drsgdev.service;

import com.github.drsgdev.dto.SignupEmail;
import com.github.drsgdev.util.SignupFailedException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender sender;
  private final SignupEmailBuilder builder;

  @Async
  public void send(SignupEmail mail) throws SignupFailedException {
    MimeMessagePreparator messagePreparator = msg -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(msg);
      messageHelper.setFrom("moviedatabase@email.com");
      messageHelper.setTo(mail.getRecipent());
      messageHelper.setSubject(mail.getSubject());
      messageHelper.setText(builder.build(mail.getBody()));
    };

    try {
      sender.send(messagePreparator);

      log.info("Send verification email to {}", mail.getRecipent());
    } catch (MailException ex) {
      log.info("Failed to send verification email to {}", mail.getRecipent());
      throw new SignupFailedException("Failed to send verification email");
    }
  }
}
