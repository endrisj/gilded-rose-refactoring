package com.gildedrose;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (Item item : items) {
            ItemCategory category = determineCategory(item.name);
            category.oneDayPassed(item);
        }
    }

    private static ItemCategory determineCategory(String name) {
        switch (name) {
            case "Sulfuras, Hand of Ragnaros":
                return ItemCategory.SULFURAS;
            case "Aged Brie":
                return ItemCategory.AGED_BRIE;
            case "Backstage passes to a TAFKAL80ETC concert":
                return ItemCategory.BACKSTAGE_PASSES;
            default:
                return ItemCategory.OTHER;
        }
    }
}
