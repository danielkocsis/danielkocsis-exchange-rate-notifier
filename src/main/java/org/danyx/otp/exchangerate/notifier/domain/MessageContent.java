package org.danyx.otp.exchangerate.notifier.domain;

import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class MessageContent {

  private final String templateName;
  private final Map<String, Object> variables;

  public MessageContent(String templateName, Map<String, Object> variables) {
    this.templateName = templateName;
    this.variables = variables;
  }
}
