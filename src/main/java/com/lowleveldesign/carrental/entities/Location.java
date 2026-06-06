package com.lowleveldesign.carrental.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private int id;
    private String area;
    private String city;
    private String state;
    private String country;
    private int pincode;
}
