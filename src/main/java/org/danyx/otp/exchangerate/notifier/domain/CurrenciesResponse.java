package org.danyx.otp.exchangerate.notifier.domain;

import java.util.List;
import lombok.Data;

@Data
public class CurrenciesResponse {

  private final Defaults defaults;
  private final List<Currency> currencies;
}

