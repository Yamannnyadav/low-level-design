package com.lowleveldesign.applycoupon.entities;

import com.lowleveldesign.applycoupon.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Product {

    private int productId;
    private ProductType productType;
    private String name;
    private double originalPrice;

    public Product(int productId, ProductType productType, String name, double originalPrice) {
        this.productId = productId;
        this.productType = productType;
        this.name = name;
        this.originalPrice = originalPrice;
    }

    public abstract double getPrice();

}
