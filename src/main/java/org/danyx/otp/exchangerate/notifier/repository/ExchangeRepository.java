package org.danyx.otp.exchangerate.notifier.repository;

import java.util.List;
import org.danyx.otp.exchangerate.notifier.domain.ExchangeResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends CrudRepository<ExchangeResponse, Long> {

  public List<ExchangeResponse> findTop2ByResultCurrencyOrderByExchangeRateDateDesc(
      String resultCurrency);
}
