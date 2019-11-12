package com.gildedrose;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

import java.util.function.BiFunction;

enum ItemCategory {

    /*
    maybe it is worth to replace if's with Ranges (one implementation is org.apache.commons.lang3.Range)
    e.g. something like that for AGED_BRIE:
    if in Range(min_integer, -1) --> quality + 2
    if in Range(0, max_integer)  --> quality + 1

    after change it would be visible, is it better, or is it over-engineered
     */

    /*
    another next possible refactoring would be to introduce Quality class,
    which would change quality by the rules from the Category.
    But also, at the moment it is difficult to say, is it would be better code.

        Quality quality {
            int value
            worthless() { value = 0 }
            increaseBy(int i) { value = min(50, value+i) }
            decreaseBy(int i) { value = max(0, value+i) }
        }
     */

    SULFURAS(0, (sellInDays, currentQuality) -> currentQuality),
    AGED_BRIE(1, (sellInDays, currentQuality) -> {
        if (sellInDays < 0) {
            return min(50, currentQuality + 2);
        }
        return min(50, currentQuality + 1);
    }),
    BACKSTAGE_PASSES(1, (sellInDays, currentQuality) -> {
        if (sellInDays < 0) {
            return 0;
        }
        if (sellInDays < 5) {
            return min(50, currentQuality + 3);
        }
        if (sellInDays < 10) {
            return min(50, currentQuality + 2);
        }
        return min(50, currentQuality + 1);
    }),
    OTHER(1, (sellInDays, currentQuality) -> {
        if (sellInDays < 0) {
            return max(0, currentQuality - 2);
        }
        return max(0, currentQuality - 1);
    });

    final int decreaseSellInDaysBy;
    private final BiFunction<Integer, Integer, Integer> qualityCalculator;

    ItemCategory(int decreaseSellInDaysBy, BiFunction<Integer, Integer, Integer> qualityCalculator) {
        this.decreaseSellInDaysBy = decreaseSellInDaysBy;
        this.qualityCalculator = qualityCalculator;
    }

    int calcNewQuality(int sellInDays, int currentQuality) {
        return qualityCalculator.apply(sellInDays, currentQuality);
    }
}
