package com.lowleveldesign.paymentsystem.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDO {

    private int id;
    private String name;
    private String mail;

    public UserDO() {

    }

    public UserDO(int id, String name, String mail) {
        this.id = id;
        this.name = name;
        this.mail = mail;
    }

}
