package org.danyx.otp.exchangerate.notifier.service;

import lombok.RequiredArgsConstructor;
import org.danyx.otp.exchangerate.notifier.domain.MessageContent;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailContentBuilderService {

  private final TemplateEngine templateEngine;

  public String build(MessageContent messageContent) {
    Context context = new Context();
    context.setVariables(messageContent.getVariables());

    return templateEngine.process(messageContent.getTemplateName(), context);
  }
}