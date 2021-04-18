package com.github.alesaudate.demostockexchange.tests.unit.domain;

import com.github.alesaudate.demostockexchange.domain.AveragePricing;
import com.github.alesaudate.demostockexchange.fixtures.AveragePricingFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.alesaudate.demostockexchange.fixtures.AveragePricingFixture.averagePricingFixture;
import static com.github.alesaudate.demostockexchange.fixtures.Randoms.moneyValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AveragePricingTest {

    @DisplayName("Given " +
            "   some random average pricing " +
            "When " +
            "   I register a new price on the object  " +
            "Then " +
            "   the value is recalculated " +
            "And " +
            "   the original object is not affected")
    @ParameterizedTest
    @MethodSource("randomPricings")
    public void testCalculatedValueIsOk(AveragePricing averagePricing) {

        var clone = AveragePricingFixture.clone(averagePricing);
        var moneyValue = moneyValue(500);
        var changed = calculateNewValue(averagePricing, moneyValue);
        assertEquals(changed,   averagePricing.registerNewPrice(moneyValue));
        assertEquals(clone, averagePricing);

    }

    private AveragePricing calculateNewValue(AveragePricing averagePricing, BigDecimal newValue) {
        var unfoldValue = averagePricing.getCurrentAverage().multiply(BigDecimal.valueOf(averagePricing.getDatapoints()));
        var sum = unfoldValue.add(newValue);
        var dividedValue = sum.divide(BigDecimal.valueOf(averagePricing.getDatapoints() + 1L), 2, RoundingMode.HALF_UP);
        return new AveragePricing(averagePricing.getStock(), dividedValue, averagePricing.getDatapoints() + 1);
    }

    private static Stream<AveragePricing> randomPricings() {
        return IntStream.range(0, 10).boxed().map(n -> averagePricingFixture());
    }

}
