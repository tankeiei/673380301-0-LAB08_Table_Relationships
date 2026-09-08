package com.example.demo.strategy;

public class MemberDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscountedPrice(double originalPrice) {
        return originalPrice * 0.90;
    }
}
