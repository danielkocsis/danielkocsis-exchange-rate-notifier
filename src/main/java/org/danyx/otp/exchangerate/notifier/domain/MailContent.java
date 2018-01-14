package org.danyx.otp.exchangerate.notifier.domain;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MailContent {

  private final String templateName;
  private final Map<String, Object> variables;
}
