package com.lowleveldesign.paymentsystem.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private int userID;
    private String userName;
    private String email;

    public User() {

    }

    public User(int userID, String userName, String email) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
    }

}
