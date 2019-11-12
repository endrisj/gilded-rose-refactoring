package com.gildedrose;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

import java.util.function.BiFunction;

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

enum ItemCategory {

    SULFURAS(0, (sellInDays, currentQuality) -> currentQuality),
    AGED_BRIE(1, (sellInDays, currentQuality) -> {
        if (sellInDays < 0) {
            return increaseQuality(currentQuality, 2);
        }
        return increaseQuality(currentQuality, 1);
    }),
    BACKSTAGE_PASSES(1, (sellInDays, currentQuality) -> {
        if (sellInDays < 0) {
            return 0;
        }
        if (sellInDays < 5) {
            return increaseQuality(currentQuality, 3);
        }
        if (sellInDays < 10) {
            return increaseQuality(currentQuality, 2);
        }
        return increaseQuality(currentQuality, 1);
    }),
    OTHER(1, (sellInDays, currentQuality) -> {
        if (sellInDays < 0) {
            return decreaseQuality(currentQuality, 2);
        }
        return decreaseQuality(currentQuality, 1);
    }),
    CONJURED(1, (sellInDays, currentQuality) -> {
        int oneTimeDegradedQuality = OTHER.qualityCalculator.apply(sellInDays, currentQuality);
        return OTHER.qualityCalculator.apply(sellInDays, oneTimeDegradedQuality);
    });

    private static final int MAX_QUALITY = 50;
    private static final int MIN_QUALITY = 0;
    private final int decreaseSellInDaysBy;
    private final BiFunction<Integer, Integer, Integer> qualityCalculator;

    ItemCategory(int decreaseSellInDaysBy, BiFunction<Integer, Integer, Integer> qualityCalculator) {
        this.decreaseSellInDaysBy = decreaseSellInDaysBy;
        this.qualityCalculator = qualityCalculator;
    }

    public static ItemCategory determineFrom(String name) {
        switch (name) {
            case "Sulfuras, Hand of Ragnaros":
                return ItemCategory.SULFURAS;
            case "Aged Brie":
                return ItemCategory.AGED_BRIE;
            case "Backstage passes to a TAFKAL80ETC concert":
                return ItemCategory.BACKSTAGE_PASSES;
            case "Conjured Mana Cake":
                return CONJURED;
            default:
                return ItemCategory.OTHER;
        }
    }

    public void oneDayPassed(Item item) {
        item.sellIn -= decreaseSellInDaysBy;
        item.quality = qualityCalculator.apply(item.sellIn, item.quality);
    }

    private static int increaseQuality(int currentQuality, int increaseBy) {
        return min(MAX_QUALITY, currentQuality + increaseBy);
    }

    private static int decreaseQuality(int currentQuality, int decreaseBy) {
        return max(MIN_QUALITY, currentQuality - decreaseBy);
    }
}
