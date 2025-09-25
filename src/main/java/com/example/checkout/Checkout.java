package com.example.checkout;

import java.util.*;
import java.util.stream.Collectors;

public final class Checkout {

    public record Totals(int totalPence) {
        public String formatGBP() {
            int pounds = totalPence / 100;
            int pence = Math.abs(totalPence % 100);
            return "£%d.%02d".formatted(pounds, pence);
        }
    }

    /** Basic total with no offers. Accepts case-insensitive item names. */
    public Totals total(List<String> scannedItems) {
        Objects.requireNonNull(scannedItems, "scannedItems");
        int sum = 0;
        for (String s : scannedItems) {
            var item = Item.fromString(s);
            sum += item.pricePence();
        }
        return new Totals(sum);
    }

    /** Total with offers: BOGO on apples, 3-for-2 on oranges. */
    public Totals totalWithOffers(List<String> scannedItems) {
        Objects.requireNonNull(scannedItems, "scannedItems");
        Map<Item, Long> counts = scannedItems.stream()
                .map(Item::fromString)
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));

        long apples = counts.getOrDefault(Item.APPLE, 0L);
        long oranges = counts.getOrDefault(Item.ORANGE, 0L);

        int chargeableApples = (int)(apples - apples / 2);       // BOGO
        int chargeableOranges = (int)(oranges - oranges / 3);    // 3-for-2

        int sum = chargeableApples * Item.APPLE.pricePence()
                + chargeableOranges * Item.ORANGE.pricePence();

        return new Totals(sum);
    }
}
