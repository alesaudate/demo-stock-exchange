package com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class Stock {

    String stock;
    BigDecimal price;


}
