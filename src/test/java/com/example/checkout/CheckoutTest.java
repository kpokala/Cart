package com.example.checkout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutTest {

    private final Checkout checkout = new Checkout();

    @Test
    @DisplayName("Basic pricing: [Apple, Apple, Orange, Apple] => £2.05")
    void basicExampleFromBrief() {
        var result = checkout.total(List.of("Apple", "Apple", "Orange", "Apple"));
        assertEquals(205, result.totalPence());
        assertEquals("£2.05", result.formatGBP());
    }

    @Test
    @DisplayName("Empty basket costs 0")
    void emptyBasket() {
        var result = checkout.total(List.of());
        assertEquals(0, result.totalPence());
        assertEquals("£0.00", result.formatGBP());
    }

    @Test
    @DisplayName("Unknown items should throw")
    void unknownItemThrows() {
        assertThrows(IllegalArgumentException.class, () -> checkout.total(List.of("Banana")));
    }

    @ParameterizedTest(name = "Offers: {0} apples -> pay for {1}")
    @CsvSource({
            "0,0",
            "1,1",
            "2,1",
            "3,2",
            "4,2",
            "5,3"
    })
    void appleBogoOffer(int apples, int expectedChargeable) {
        var basket = java.util.Collections.nCopies(apples, "Apple");
        var result = checkout.totalWithOffers(basket);
        assertEquals(expectedChargeable * Item.APPLE.pricePence(), result.totalPence());
    }

    @ParameterizedTest(name = "Offers: {0} oranges -> pay for {1}")
    @CsvSource({
            "0,0",
            "1,1",
            "2,2",
            "3,2",
            "4,3",
            "5,4",
            "6,4"
    })
    void orangeThreeForTwoOffer(int oranges, int expectedChargeable) {
        var basket = java.util.Collections.nCopies(oranges, "Orange");
        var result = checkout.totalWithOffers(basket);
        assertEquals(expectedChargeable * Item.ORANGE.pricePence(), result.totalPence());
    }

    @Test
    @DisplayName("Mixed basket with offers applied correctly")
    void mixedBasketOffers() {
        var result = checkout.totalWithOffers(List.of("Apple", "Apple", "Orange", "Apple"));
        // Apples: 3 -> pay for 2 (2*60=120), Oranges: 1 -> pay for 1 (25). Total = 145 pence.
        assertEquals(145, result.totalPence());
        assertEquals("£1.45", result.formatGBP());
    }

    @Test
    @DisplayName("Case-insensitive item names and simple plurals")
    void caseInsensitiveAndPlurals() {
        var result = checkout.totalWithOffers(List.of("apples", "APPLE", "OrAnGeS"));
        // Apples: 2 -> pay for 1 (60), Oranges: 1 -> pay for 1 (25). Total = 85 pence.
        assertEquals(85, result.totalPence());
    }
}
