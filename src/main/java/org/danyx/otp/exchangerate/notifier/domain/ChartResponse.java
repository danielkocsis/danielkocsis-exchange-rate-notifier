package org.danyx.otp.exchangerate.notifier.domain;

import lombok.Data;

@Data
public class ChartResponse {

  private final Currency[] currencies;
}
