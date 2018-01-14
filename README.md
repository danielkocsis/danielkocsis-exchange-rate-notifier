# Exchange rate notifier

This small Spring Boot application is meant to query (and store) currency exchange rates of OTP bank and to send daily notification for the subscribed customers about the recent changes.

## Version history

* 1.0 - Development is currently in progress. Goals of the fist version:
  1. Deploy the application to AWS
  1. Be able to send HTML email through Google Mail SMTP
  1. Be able to handle notification subscription via REST API's
