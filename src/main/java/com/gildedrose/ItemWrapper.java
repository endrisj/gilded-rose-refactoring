package com.gildedrose;

class ItemWrapper {

    private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    private static final String AGED_BRIE = "Aged Brie";
    private static final String BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert";

    private enum ItemCategory {
        SULFURAS(0),
        AGED_BRIE(1),
        BACKSTAGE_PASSES(1),
        OTHER(1);

        private final int decreaseSellInDaysBy;

        ItemCategory(int decreaseSellInDaysBy) {
            this.decreaseSellInDaysBy = decreaseSellInDaysBy;
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
        if (!item.name.equals(AGED_BRIE) && !item.name.equals(BACKSTAGE_PASSES)) {
            if (item.quality > 0) {
                if (!item.name.equals(SULFURAS)) {
                    item.quality = item.quality - 1;
                }
            }
        } else {
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
        }

        if (item.sellIn < 0) {
            if (!item.name.equals(AGED_BRIE)) {
                if (!item.name.equals(BACKSTAGE_PASSES)) {
                    if (item.quality > 0) {
                        if (!item.name.equals(SULFURAS)) {
                            item.quality = item.quality - 1;
                        }
                    }
                } else {
                    item.quality = 0;
                }
            } else {
                if (item.quality < 50) {
                    item.quality = item.quality + 1;
                }
            }
        }
    }
}
