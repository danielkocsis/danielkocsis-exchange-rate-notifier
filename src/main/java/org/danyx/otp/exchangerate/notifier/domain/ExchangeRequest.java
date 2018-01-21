package org.danyx.otp.exchangerate.notifier.domain;

import lombok.Data;

/**
 * EXAMPLE JSON
 *
 * { baseAmount:1 baseCurrency:"HUF" exchangeType:"CURRENCY" resultCurrency:"USD" }
 */
@Data
public class ExchangeRequest {

  private final double baseAmount;
  private final String baseCurrency;
  private final ExchangeType exchangeType;
  private final String resultCurrency;

  public enum ExchangeType {
    FOREIGN_EXCHANGE, CURRENCY
  }
}