package com.lowleveldesign.applycoupon.coupondecorator;

import com.lowleveldesign.applycoupon.entities.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CouponDecorator extends Product {

    private Product product;
    private int discountPercentage;


    public CouponDecorator(Product product, int discountPercentage) {
        super(product.getProductId(), product.getProductType(), product.getName(), product.getOriginalPrice());
        this.product = product;
        this.discountPercentage = discountPercentage;
    }
}
