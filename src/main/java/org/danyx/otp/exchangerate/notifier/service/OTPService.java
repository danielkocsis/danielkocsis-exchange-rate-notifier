package org.danyx.otp.exchangerate.notifier.service;

import com.google.gson.Gson;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.danyx.otp.exchangerate.notifier.domain.CurrenciesResponse;
import org.danyx.otp.exchangerate.notifier.domain.ExchangeRequest;
import org.danyx.otp.exchangerate.notifier.domain.ExchangeRequest.ExchangeType;
import org.danyx.otp.exchangerate.notifier.domain.ExchangeResponse;
import org.danyx.otp.exchangerate.notifier.repository.ExchangeRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OTPService {

  private static final String EXCHANGE_API_BASE_URL =
      "https://www.otpbank.hu/apps/exchangerate/api/exchangerate";

  private final WebClient webClient;
  private final ExchangeRepository exchangeRepository;
  private final Gson gson = new Gson();
  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private final String EXCHANGE_SERVICE = "/exchange";
  private final String CURRENCIES_SERVICE = "/currencies";
  private final String CHART_SERVICE = "/chart";

  public OTPService(ExchangeRepository exchangeRepository) {
    this.exchangeRepository = exchangeRepository;

    webClient = WebClient.builder()
        .baseUrl(EXCHANGE_API_BASE_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE)
        .build();
  }

  public List<String> getCurrencies() {
    RequestHeadersSpec<?> request =
        webClient
            .get()
            .uri(CURRENCIES_SERVICE)
            .acceptCharset(Charset.forName("UTF-8"));

    Mono<String> body = request.retrieve().bodyToMono(String.class);

    CurrenciesResponse currenciesResponse =
        body.map(b -> gson.fromJson(b, CurrenciesResponse.class)).block();

    log.debug("Response from service call " + CURRENCIES_SERVICE + "\n" + currenciesResponse);

    return currenciesResponse.getCurrencies().stream()
        .map(c -> c.getCurrencyCode())
        .collect(Collectors.toList());
  }

  /**
   * napi tobb version mindenhez
   */
  public List<?> getAllOTPRates(LocalDate date) {
    RequestHeadersSpec<?> request =
        webClient
            .get()
            .uri("/otp/" + date.format(dateTimeFormatter))
            .acceptCharset(Charset.forName("UTF-8"));

    Mono<String> body = request.retrieve().bodyToMono(String.class);

    log.debug("\n\nResponse from service call " + CURRENCIES_SERVICE + "\n" + body.block());

    return Collections.emptyList();
  }

  public ExchangeResponse exchange(String baseCurrency, String resultCurrency, double amount) {
    ExchangeRequest exchangeRequest = new ExchangeRequest(
        amount, baseCurrency, ExchangeType.FOREIGN_EXCHANGE, resultCurrency);

    RequestHeadersSpec<?> request =
        webClient
            .post()
            .uri(EXCHANGE_SERVICE)
            .acceptCharset(Charset.forName("UTF-8"))
            .body(BodyInserters.fromObject(exchangeRequest));

    Mono<String> body = request.retrieve().bodyToMono(String.class);

    ExchangeResponse exchangeResponse = gson.fromJson(body.block(), ExchangeResponse.class);

    log.debug("Response from service call " + EXCHANGE_SERVICE + "\n" + exchangeResponse);

    return exchangeResponse;
  }

  public ExchangeResponse exchangeAndPersist(
      String baseCurrency, String resultCurrency, double amount) {

    ExchangeResponse exchangeResponse = exchange(baseCurrency, resultCurrency, amount);

    exchangeRepository.save(exchangeResponse);

    return exchangeResponse;
  }

  public List<ExchangeResponse> getLatestTwoExchangeResponses(String resultCurrency) {
    return exchangeRepository.findTop2ByResultCurrencyOrderByExchangeRateDateDesc(resultCurrency);
  }

  public List<ExchangeResponse> getExchangeHistory(String resultCurrency) {
    List<ExchangeResponse> responses = new ArrayList<>();

    exchangeRepository.findAll().forEach(responses::add);

    return responses;
  }
}
