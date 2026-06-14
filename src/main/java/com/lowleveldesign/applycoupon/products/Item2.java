package com.lowleveldesign.applycoupon.products;

import com.lowleveldesign.applycoupon.entities.Product;
import com.lowleveldesign.applycoupon.enums.ProductType;

public class Item2 extends Product {

    public Item2(int productId, ProductType productType, String name, double originalPrice) {
        super(productId, productType, name, originalPrice);
    }


    @Override
    public double getPrice() {
        return getOriginalPrice();
    }

}
