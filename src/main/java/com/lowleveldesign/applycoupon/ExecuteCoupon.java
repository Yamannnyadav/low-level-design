package com.lowleveldesign.applycoupon;

import com.lowleveldesign.applycoupon.entities.Cart;
import com.lowleveldesign.applycoupon.entities.Product;
import com.lowleveldesign.applycoupon.enums.ProductType;
import com.lowleveldesign.applycoupon.products.Item1;
import com.lowleveldesign.applycoupon.products.Item2;
import com.lowleveldesign.applycoupon.products.Item3;

public class ExecuteCoupon {

    public static void main (String[] args) {

        System.out.println("\n###### LLD - Coupon Application System Demo ######\n");

        // Create Products
        Product item1 = new Item1(101, ProductType.ELECTRONICS, "fan", 1500);
        Product item2 = new Item2(102, ProductType.FURNITURE, "Office chair", 5500);
        Product item3 = new Item3(103, ProductType.CLOTHES, "shirt", 750);

        // Shopping Cart
        Cart cart = new Cart();
        cart.addToCart(item1);
        cart.addToCart(item2);
        cart.addToCart(item3);

        // Calculate Total Price
        System.out.println("\n===>>> Total Price after discount: " + cart.getTotalPrice());

    }
}
