package com.lowleveldesign.applycoupon.coupondecorator;

import com.lowleveldesign.applycoupon.entities.Product;
import com.lowleveldesign.applycoupon.enums.ProductType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TypeCouponDecorator extends CouponDecorator {

    private static final Set<ProductType> eligibleProductTypes =
            Set.of(
                    ProductType.FURNITURE,
                    ProductType.ELECTRONICS
            );

    public TypeCouponDecorator(Product product, int percentage) {
        super(product, percentage);
    }

    @Override
    public double getPrice() {
        double price = this.getProduct().getPrice();
        if (eligibleProductTypes.contains(this.getProduct().getProductType())) {
            double priceAfterDiscount = price - (price * getDiscountPercentage()) / 100;
            System.out.println("Applying specific product type coupon of " + getDiscountPercentage() + "% on " + getProduct().getName() + ", original price : " + price + ", price after discount : " + priceAfterDiscount);
            return priceAfterDiscount;
        }
        return price;
    }
}
