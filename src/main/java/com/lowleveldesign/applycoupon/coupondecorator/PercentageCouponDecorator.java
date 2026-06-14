package com.lowleveldesign.applycoupon.coupondecorator;

import com.lowleveldesign.applycoupon.entities.Product;

public class PercentageCouponDecorator extends CouponDecorator {

    public PercentageCouponDecorator(Product product, int percentage) {
        super(product, percentage);
    }


    @Override
    public double getPrice() {
        double price = this.getProduct().getPrice();
        double priceAfterDiscount = price - (getDiscountPercentage()*price)/100;
        System.out.println("Applying percentage coupon of " + getDiscountPercentage() + "% on " + getProduct().getName() + ", original price : " + price + ", price after discount : " + priceAfterDiscount);
        return  priceAfterDiscount;
    }
}
