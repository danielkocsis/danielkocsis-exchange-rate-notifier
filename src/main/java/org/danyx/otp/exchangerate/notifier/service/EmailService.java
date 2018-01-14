package org.danyx.otp.exchangerate.notifier.service;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.danyx.otp.exchangerate.notifier.domain.MailContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final MailContentBuilder mailContentBuilder;

  @Value("${mail.notification.to}")
  private String[] to;
  @Value("${mail.notification.from}")
  private String from;
  @Value("${mail.notification.subject}")
  private String subject;
  @Value("${mail.notification.enabled:false}")
  private boolean notificationEnabled;

  public void sendNotification(Supplier<MailContent> mailContentSupplier) {
    if (!notificationEnabled) {
      log.debug("Email notifications are disabled! Skipping to send notification to " + to);

      return;
    }

    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(
          mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
          StandardCharsets.UTF_8.name());

      messageHelper.setFrom(from);
      messageHelper.setTo(to);
      messageHelper.setSubject(subject);
      messageHelper.setText(mailContentBuilder.build(mailContentSupplier.get()), true);
    };

    try {
      mailSender.send(messagePreparator);
    } catch (MailException e) {
      log.error("Unable to send email to " + to, e);
    }
  }
}
