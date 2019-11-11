package com.gildedrose;

import static java.lang.Integer.max;

import java.util.function.BiFunction;

class ItemWrapper {

    private static final String AGED_BRIE = "Aged Brie";
    private static final String BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert";

    // TODO aj: move to separate class
    private enum ItemCategory {
        SULFURAS(0, (sellInDays, currentQuality) -> currentQuality),
        AGED_BRIE(1, (sellInDays, currentQuality) -> {
            return 1;
        }),
        BACKSTAGE_PASSES(1, (sellInDays, currentQuality) -> {
            return 1;
        }),
        OTHER(1, (sellInDays, currentQuality) -> {
            if (sellInDays < 0) {
                return max(0, currentQuality - 2);
            }
            return max(0, currentQuality - 1);
        });

        private final int decreaseSellInDaysBy;
        private final BiFunction<Integer, Integer, Integer> qualityCalculator;

        ItemCategory(int decreaseSellInDaysBy, BiFunction<Integer, Integer, Integer> qualityCalculator) {
            this.decreaseSellInDaysBy = decreaseSellInDaysBy;
            this.qualityCalculator = qualityCalculator;
        }

        int calcNewQuality(int sellInDays, int currentQuality) {
            return qualityCalculator.apply(sellInDays, currentQuality);
        }
    }

    private final Item item;
    private final ItemCategory category;

    ItemWrapper(Item item) {
        this.item = item;
        switch (item.name) {
            case "Sulfuras, Hand of Ragnaros":
                category = ItemCategory.SULFURAS;
                break;
            case "Aged Brie":
                category = ItemCategory.AGED_BRIE;
                break;
            case "Backstage passes to a TAFKAL80ETC concert":
                category = ItemCategory.BACKSTAGE_PASSES;
                break;
            default:
                category = ItemCategory.OTHER;
        }
    }

    void decreaseSellInDays() {
        item.sellIn -= category.decreaseSellInDaysBy;
        updateQuality();
    }

    private void updateQuality() {
        if (ItemCategory.SULFURAS == category || ItemCategory.OTHER == category) {
            item.quality = category.calcNewQuality(item.sellIn, item.quality);
            return;
        }


        if (item.quality < 50) {
            item.quality = item.quality + 1;

            if (item.name.equals(BACKSTAGE_PASSES)) {
                if (item.sellIn < 10) {
                    if (item.quality < 50) {
                        item.quality = item.quality + 1;
                    }
                }

                if (item.sellIn < 5) {
                    if (item.quality < 50) {
                        item.quality = item.quality + 1;
                    }
                }
            }
        }

        if (item.sellIn < 0) {
            if (!item.name.equals(AGED_BRIE)) {
                item.quality = 0;
            } else {
                if (item.quality < 50) {
                    item.quality = item.quality + 1;
                }
            }
        }
    }
}
