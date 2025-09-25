package com.example.checkout;

import java.util.Locale;

public enum Item {
    APPLE(60),
    ORANGE(25);

    private final int pricePence;

    Item(int pricePence) {
        this.pricePence = pricePence;
    }

    public int pricePence() {
        return pricePence;
    }

    public static Item fromString(String s) {
        if (s == null) throw new IllegalArgumentException("Item string is null");
        var normalized = s.trim().toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "APPLE", "APPLES" -> APPLE;
            case "ORANGE", "ORANGES" -> ORANGE;
            default -> throw new IllegalArgumentException("Unknown item: " + s);
        };
    }
}
