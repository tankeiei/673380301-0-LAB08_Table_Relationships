package com.example.demo.strategy;

public class SeasonalSaleStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscountedPrice(double originalPrice) {
        return originalPrice * 0.80;
    }
}
