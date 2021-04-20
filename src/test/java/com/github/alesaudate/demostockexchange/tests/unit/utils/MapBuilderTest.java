package com.github.alesaudate.demostockexchange.tests.unit.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.github.alesaudate.demostockexchange.fixtures.Randoms.randomString;
import static com.github.alesaudate.demostockexchange.utils.MapBuilder.map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapBuilderTest {

    @DisplayName("Given " +
            "   a map builder " +
            "When " +
            "   I build a map without having added any value " +
            "Then " +
            "   an empty map must be built ")
    @Test
    public void testEmptyMap() {

        assertEquals(Map.of(), map().build());

    }


    @DisplayName("Given " +
            "   a map builder " +
            "When " +
            "   I build a map with a few entries " +
            "Then " +
            "   a map must be built with the corresponding entries")
    @Test
    public void testBuildMapWithFewEntries() {

        var key1 = randomString(10);
        var value1 = randomString(10);

        var key2 = randomString(20);
        var value2  = randomString(20);

        var key3 = randomString(30);
        var value3 = randomString(40);



        assertEquals(Map.of(key1, value1, key2, value2, key3, value3),
                map()
                        .entry(key1, value1)
                        .entry(key2, value2)
                        .entry(key3, value3)
                        .build());

    }


    @DisplayName("Given " +
            "   a map builder " +
            "When " +
            "   I build a map with a few entries using functions " +
            "Then " +
            "   a map must be built with the corresponding entries")
    @Test
    public void testBuildMapWithFewEntriesCallingFunctions() {

        var key1 = randomString(10);
        var value1 = randomString(10);

        var key2 = randomString(20);
        var value2  = randomString(20);

        var key3 = randomString(30);
        var value3 = randomString(40);



        assertEquals(Map.of(key1, value1, key2, value2, key3, value3),
                map()
                        .entry(key1, k -> value1)
                        .entry(key2, k -> value2)
                        .entry(key3, k -> value3)
                        .build());

    }




}
