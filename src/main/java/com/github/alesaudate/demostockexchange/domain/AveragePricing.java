package com.github.alesaudate.demostockexchange.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * This object is meant to be immutable, in order to work well with multi-threading
 */

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class AveragePricing {

    String stock;
    BigDecimal currentAverage;
    long datapoints;

    public AveragePricing registerNewPrice(BigDecimal price) {
        price = price.setScale(2, RoundingMode.HALF_UP);
        var unfoldedAverage = currentAverage.multiply(BigDecimal.valueOf(datapoints));
        var newSum = unfoldedAverage.add(price).divide(BigDecimal.valueOf(datapoints + 1), RoundingMode.HALF_UP);
        return new AveragePricing(this.stock, newSum, datapoints + 1);
    }
}
