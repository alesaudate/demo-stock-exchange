package com.github.alesaudate.demostockexchange.interfaces.outcoming;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
public class Stock {

    String stock;
    BigDecimal price;


}
