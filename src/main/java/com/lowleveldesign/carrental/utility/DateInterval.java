package com.lowleveldesign.carrental.utility;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DateInterval {

    private LocalDate from;
    private LocalDate to;

    public DateInterval(LocalDate from, LocalDate to){
        if(to.isBefore(from)){
            throw new IllegalArgumentException("From Date cannot be greater than To Date");
        }
        this.from = from;
        this.to = to;
    }

    public boolean checkIfOverlaps(DateInterval other) {
        return !(this.to.isBefore(other.from) || this.from.isAfter(other.to));
        //negates two non-overlapping cases
    }

}
