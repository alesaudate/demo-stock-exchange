package com.github.alesaudate.demostockexchange.domain;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties
public class Stock {

    String value;

}
