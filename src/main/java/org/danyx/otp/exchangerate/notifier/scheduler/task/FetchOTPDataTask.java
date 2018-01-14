package org.danyx.otp.exchangerate.notifier.scheduler.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.danyx.otp.exchangerate.notifier.domain.ExchangeResponse;
import org.danyx.otp.exchangerate.notifier.domain.MailContent;
import org.danyx.otp.exchangerate.notifier.service.EmailService;
import org.danyx.otp.exchangerate.notifier.service.OTPService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FetchOTPDataTask {

  private final EmailService emailService;
  private final OTPService otpService;

  @Value("${scheduledJob.enabled:false}")
  private boolean scheduledJobEnabled;

  public FetchOTPDataTask(EmailService emailService, OTPService otpService) {
    this.emailService = emailService;
    this.otpService = otpService;
  }

  /**
   * runs at 8am, 12pm and 16pm of every workday of a week
   */
  //@Scheduled(cron = "0 0 */3 * * MON-FRI")
  @Scheduled(fixedRate = 10000)
  public void runScheduled() {
    if (!scheduledJobEnabled) {
      log.debug("Fetch OTP data is disable, quitting from the process...");

      return;
    }

    otpService.exchangeAndPersist("USD", "HUF", 1);

    emailService.sendNotification(this::prepareOTPNotificationMailContent);

    log.debug("Scheduled job successfully finished");
  }

  public MailContent prepareOTPNotificationMailContent() {
    ExchangeResponse latest = null;
    ExchangeResponse previous = null;

    try {
      List<ExchangeResponse> previousExchangeResponses =
          otpService.getLatestTwoExchangeResponses("HUF");

      latest = previousExchangeResponses.get(0);
      previous = previousExchangeResponses.get(1);
    }
    catch (Exception e) {
      log.error("An error occurred during fetching the latest exchange rates!", e);

      latest = previous = new ExchangeResponse();
    }

    if (log.isDebugEnabled()) {
      log.debug("1 USD is " + latest.getResultAmount() + " HUF right now!");
    }

    Map<String, Object> variables = new HashMap<>();
    variables.put("latestExchangeData", latest);
    variables.put("previousExchangeData", previous);

    return new MailContent("mailTemplate", variables);
  }
}
