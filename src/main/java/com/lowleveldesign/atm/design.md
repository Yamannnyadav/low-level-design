# ATM

**Problem Statement**:
We need to design an ATM where user can withdraw and deposit money.

**Functional Requirements**:
- User should be able to withdraw money.
- User should be able to deposit money.
- User should be able to check account balance.


**Non-Function Requirements**:
- System should choose consistency over availability as money correctness is important.
- System should be reliable and secure.


**Core Entities**:
- ATM
- User
- Card
- Transaction
- Bank Account


Entities and their relationship:

ATM 
- ATM states
- noOfTwoThousandNotes
- noOfFiveHundredNotes
- noOfHundredNotes
- atmBalance


User
- card
- userBankAccount


Card
- cardNumber
- expiryDate
- cvv
- cardProviderName


UserBankAccount
- accountBalance
- bankName
- transactionDetails


Transaction
- transactionAmount


**Patterns Used**:
- ATM will have states so it uses state design pattern.
- ATM will have to fulfil request depending on different denomination so it has
Chain of Responsibility pattern.