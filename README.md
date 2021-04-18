# Stock exchange (demo)

<!-- TODO: incluir badges da parte de cobertura -->
<!-- Badges: build status, code coverage -->

This project is about exposing API's that are capable of streaming the average price of some given stocks.

It is capable of both generating random, artificial values for the stocks, as well as consuming an API to retrieve their actual value. 

## Technology

This project is based on Spring WebFlux and reactive programming. As such, it uses its API's to fetch the data and start the streaming (which will ultimately reach the application client)

## What's contained in the package

- [Integration tests configured](gradle/integrationTest.gradle)
- [Coverage tests configured](gradle/coverage.gradle)
