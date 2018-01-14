package org.danyx.otp.exchangerate.notifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExchangeRateNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRateNotifierApplication.class, args);
	}
}
