# DESIGN PAYMENT SYSTEM

## Problem Statement

Design a Payment System which acts as mediator between the User and Financial Institution and helps
transfer money.

---

# Functional Requirements

* User should be able to initiate a payment.
* User should be able to choose a Payment Method.
* User should be able to track status of Payment.


---

# Non-Functional Requirements

* The system should ensure consistency of the payment state.
* The design should be extensible to support additional payment methods in the future.

---

# Core Entities

* User
* Payment Method (Instrument like UPI, Card etc.)
* Transaction
* Payment (one payment can have many transactions (failed, success))
* Status


---

# Entities and Their Relationships

## Instrument

```
int instrumentID;
int userID;
InstrumentType instrumentType;
```

---
