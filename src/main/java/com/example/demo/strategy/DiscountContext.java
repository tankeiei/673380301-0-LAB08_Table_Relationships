package com.example.demo.strategy;

public class DiscountContext {
    public static DiscountStrategy getStrategy(String discountType) {
        if ("MEMBER".equalsIgnoreCase(discountType)) {
            return new MemberDiscountStrategy();
        } else if ("SEASONAL".equalsIgnoreCase(discountType)) {
            return new SeasonalSaleStrategy();
        }
        return new NoDiscountStrategy();
    }
}
