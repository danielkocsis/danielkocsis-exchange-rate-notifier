package org.danyx.otp.exchangerate.notifier.service;

import lombok.RequiredArgsConstructor;
import org.danyx.otp.exchangerate.notifier.domain.MailContent;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailContentBuilder {

  private final TemplateEngine templateEngine;

  public String build(MailContent mailContent) {
    Context context = new Context();
    context.setVariables(mailContent.getVariables());

    return templateEngine.process(mailContent.getTemplateName(), context);
  }
}