package com.lowleveldesign.applycoupon.entities;

import com.lowleveldesign.applycoupon.coupondecorator.PercentageCouponDecorator;
import com.lowleveldesign.applycoupon.coupondecorator.TypeCouponDecorator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Cart {

    private List<Product> productList;

    public Cart(){
        productList = new ArrayList<>();
    }

    public void addToCart(Product product){
        // Decorate the Product with applicable coupons
        Product productWithEligibleDiscount =
                new TypeCouponDecorator(new PercentageCouponDecorator(product, 20), 10);
        productList.add(productWithEligibleDiscount);
    }

    public double getTotalPrice() {

        double totalPrice = 0;

        // productList contains decorated products
        for(Product product : productList){
            totalPrice += product.getPrice(); // getPrice() will return the price after applying the coupons
        }

        return totalPrice;

    }

}
