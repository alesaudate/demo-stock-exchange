package com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class Stock {

    String stock;
    BigDecimal price;


}
