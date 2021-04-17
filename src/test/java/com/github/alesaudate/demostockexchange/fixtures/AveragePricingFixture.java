package com.github.alesaudate.demostockexchange.fixtures;

import com.github.alesaudate.demostockexchange.domain.AveragePricing;

import static com.github.alesaudate.demostockexchange.fixtures.Randoms.*;

public class AveragePricingFixture {


    public static AveragePricing averagePricingFixture() {
        return new AveragePricing(randomNYSEStock(), moneyValue(500), randomLong(20));
    }

    public static AveragePricing clone(AveragePricing instance) {
        return new AveragePricing(instance.getStock(), instance.getCurrentAverage(), instance.getDatapoints());
    }
}
