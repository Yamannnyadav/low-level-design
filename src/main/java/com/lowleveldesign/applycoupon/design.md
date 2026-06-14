# COUPON APPLICATION

**Problem Statement**:
Design a detailed low-level-design of Coupon application where user can avail discounts on products
using coupon.


**Functional Requirements**:
- User should be able to apply a coupon during checkout.
- Coupon should have an expiry date.
- System should validate coupon eligibility before applying.
- System should calculate and return the discounted amount.
- A coupon can be used only once globally.


**Non-Function Requirements**:
- System should ensure strong consistency while consuming coupons.
- Coupon redemption should be atomic.
- Concurrent requests should not allow the same coupon to be redeemed multiple times.


**Core Entities**:
- User
- Coupon
- Product
- Cart


**Entities and their relationship:**

User
- id
- Coupon coupon


Product
- id
- name
- price
- enum productType


Cart
- List<Product>


**Patterns Used**:
- Decorator Pattern for different types of coupon application.
