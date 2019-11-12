package com.gildedrose

import spock.lang.Specification
import spock.lang.Unroll

class GildedRoseSpec extends Specification {

    @Unroll
    def 'updates `#name` sellIn from `#sellInDays` to `#sellInDaysExpected` after `#days` day(s)'() {
        expect:
            Item item = new Item(name, sellInDays, quality)
            GildedRose gildedRose = new GildedRose(item)
            days.times { gildedRose.updateQuality() }
            item.sellIn == sellInDaysExpected

        where:
            name                         | sellInDays | quality | days || sellInDaysExpected
            "Aged Brie"                  | 2          | 2       | 1    || 1
            "Aged Brie"                  | 2          | 2       | 9    || -7
            "Aged Brie"                  | -2         | 2       | 4    || -6
            "Sulfuras, Hand of Ragnaros" | 0          | 80      | 1    || 0
            "Sulfuras, Hand of Ragnaros" | 5          | 80      | 10   || 5
            "Sulfuras, Hand of Ragnaros" | -5         | 80      | 1    || -5
    }

    @Unroll
    def 'updates `#name` with sellIn `#sellInDays` quality from `#quality` to `#qualityExpected` after `#days` day(s). Verifying rule: `#rule`'() {
        expect:
            Item item = new Item(name, sellInDays, quality)
            GildedRose gildedRose = new GildedRose(item)
            days.times { gildedRose.updateQuality() }
            item.quality == qualityExpected

        where:
            name                                        | sellInDays | quality | days | rule                                                                                                || qualityExpected
            "+5 Dexterity Vest"                         | 10         | 20      | 1    | 'At the end of each day our system lowers both values for every item :: after 1 day'                || 19
            "+5 Dexterity Vest"                         | 10         | 20      | 8    | 'At the end of each day our system lowers both values for every item :: after n days'               || 12
            "+5 Dexterity Vest"                         | 0          | 10      | 1    | 'Once the sell by date has passed, Quality degrades twice as fast'                                  || 8
            "+5 Dexterity Vest"                         | 5          | 0       | 10   | 'The Quality of an item is never negative'                                                          || 0
            "Sulfuras, Hand of Ragnaros"                | 1          | 77      | 5    | '"Sulfuras", being a legendary item, never has to be sold or decreases in Quality'                  || 77
            "Aged Brie"                                 | 2          | 0       | 1    | '"Aged Brie" actually increases in Quality the older it gets :: sellIn positive'                    || 1
            "Aged Brie"                                 | 1          | 0       | 2    | '"Aged Brie" actually increases in Quality the older it gets :: sellIn negative'                    || 3
            "Aged Brie"                                 | -5         | 40      | 15   | 'The Quality of an item is never more than 50'                                                      || 50
            "Backstage passes to a TAFKAL80ETC concert" | 5          | 49      | 1    | 'The Quality of an item is never more than 50'                                                      || 50
            "Backstage passes to a TAFKAL80ETC concert" | 15         | 20      | 1    | '"Backstage passes", increases in Quality as its SellIn value approaches'                           || 21
            "Backstage passes to a TAFKAL80ETC concert" | 11         | 20      | 3    | '"Backstage passes", increases in Quality as its SellIn value approaches :: Quality increases by 2' || 25
            "Backstage passes to a TAFKAL80ETC concert" | 6          | 10      | 2    | '"Backstage passes", increases in Quality as its SellIn value approaches :: Quality increases by 3' || 15
            "Backstage passes to a TAFKAL80ETC concert" | 3          | 40      | 4    | '"Backstage passes" Quality drops to 0 after the concert'                                           || 0
            "Conjured Mana Cake"                        | 5          | 25      | 1    | '"Conjured" items degrade in Quality twice as fast as normal items :: after 1 day'                  || 23
            "Conjured Mana Cake"                        | 5          | 25      | 4    | '"Conjured" items degrade in Quality twice as fast as normal items :: after n days'                 || 17
            "Conjured Mana Cake"                        | 1          | 25      | 2    | '"Conjured" items degrade in Quality twice as fast as normal items :: after sell by date passed'    || 19
    }

}
