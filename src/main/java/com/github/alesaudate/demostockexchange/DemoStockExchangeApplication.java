package com.github.alesaudate.demostockexchange;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info =
	@Info(title = "Stock Exchange (Demo)", description = "This application retrieves the average pricing for a given stock",
	license = @License(name = "MIT License", url = "https://mit-license.org/"))
)
public class DemoStockExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoStockExchangeApplication.class, args);
	}

}
