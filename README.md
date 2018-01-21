# Exchange rate notifier

This small Spring Boot application is meant to query (and store) currency exchange rates of OTP bank and to send daily notification for the subscribed customers about the recent changes.

## Version history

* 1.0 - Development is currently in progress. Goals of the fist version:
  1. Deploy the application to AWS
  1. Be able to send HTML email through Google Mail SMTP
  1. Be able to handle notification subscription via REST API's

## How to run the application?

You need to provide a valid SMTP configuration through the `application.properties` file. As the SMTP configuration contains a password, I decided to use [Jasypt](http://www.jasypt.org/)(more precisly [Jasypt Spring Boot](https://github.com/ulisesbocchio/jasypt-spring-boot)) to store encrypted properties to prevent my passwords get stolen. 

Practically it means to store sensitive properties like this: `secret.property=ENC(xxx)`, where `xxx` is the encrypted form of the original value returned by [Jasypt CLI](http://www.jasypt.org/cli.html). To do this you also need to define an encryptor password, which you also need to provide Jasypt during the application execution.

To do this, we can pass the encryptor key as a command like argument(or you can pass it as an environment variable): `gradlew -Djasypt.encryptor.password={yourEncryptorPassword} clean bootRun`
