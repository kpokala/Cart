package com.example.checkout;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var checkout = new Checkout();
        var basket = List.of("Apple", "Apple", "Orange", "Apple");
        var noOffers = checkout.total(basket);
        var withOffers = checkout.totalWithOffers(basket);

        System.out.println("Basket: " + basket);
        System.out.println("Total (no offers): " + noOffers.formatGBP());
        System.out.println("Total (with offers): " + withOffers.formatGBP());
    }
}
