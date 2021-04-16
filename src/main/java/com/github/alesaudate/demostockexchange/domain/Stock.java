package com.github.alesaudate.demostockexchange.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class Stock {

    String value;

}
