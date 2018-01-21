package org.danyx.otp.exchangerate.notifier.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

/**
 * { "baseCurrency":"HUF", "resultCurrency":"USD", "baseAmount":1.0, "resultAmount":0.0036946721,
 * "exchangeRateDate":"2017-12-21T12:57:09.000+01:00", "exchangeRateVersion":2,
 * "exchangeType":"CURRENCY" }
 */
@Data
@Entity
public class ExchangeResponse {

  private String baseCurrency;
  private String resultCurrency;
  private long baseAmount;
  private double resultAmount;
  @Id
  private Date exchangeRateDate;
  private int exchangeRateVersion;
  private String exchangeType;
}