package org.danyx.otp.exchangerate.notifier.service;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.danyx.otp.exchangerate.notifier.domain.MessageContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final MailContentBuilderService mailContentBuilderService;

  @Value("${mail.notification.to}")
  private String[] to;
  @Value("${mail.notification.from}")
  private String from;
  @Value("${mail.notification.subject}")
  private String subject;
  @Value("${mail.notification.enabled:false}")
  private boolean notificationEnabled;

  public void sendNotification(Supplier<MessageContent> messageContentSupplier) {
    if (!notificationEnabled) {
      log.debug("Email notifications are disabled!");

      return;
    }

    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(
          mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
          StandardCharsets.UTF_8.name());

      messageHelper.setFrom(from);
      messageHelper.setTo(to);
      messageHelper.setSubject(subject);
      messageHelper.setText(mailContentBuilderService.build(messageContentSupplier.get()), true);
    };

    try {
      mailSender.send(messagePreparator);
    } catch (MailException e) {
      log.error("Unable to send email to " + to, e);
    }
  }
}
