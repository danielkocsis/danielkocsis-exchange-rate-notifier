package org.danyx.otp.exchangerate.notifier.domain;

import java.util.Date;
import lombok.Data;

/**
 * chequeBuyingRate:307.67 chequeSellingRate:318.13 currencyBuyingRate:304.14
 * currencySellingRate:321.66 foreignExchangeBuyingRate:309.77 foreignExchangeSellingRate:316.03
 * middleRate:312.9 validityDate:"2017-12-20T05:56:00.000+01:00" currencyCode:"EUR"
 * hungarianName:"Euro" englishName:"Euro" unitSize:1 version:1
 */
@Data
public class Currency {

  private final double chequeBuyingRate;
  private final double chequeSellingRate;
  private final double currencyBuyingRate;
  private final double currencySellingRate;
  private final double foreignExchangeBuyingRate;
  private final double foreignExchangeSellingRate;
  private final double middleRate;
  private final Date validityDate;
  private final String currencyCode;
  private final String hungarianName;
  private final String englishName;
  private final int unitSize;
  private final int version;
}
