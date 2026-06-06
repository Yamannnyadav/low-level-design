package com.lowleveldesign.carrental.reservations;

import com.lowleveldesign.carrental.enums.ReservationStatus;
import com.lowleveldesign.carrental.enums.ReservationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Reservation {

    private int reservationId;
    private int vehicleId;
    private int userId;
    private LocalDate dateBookedFrom;
    private LocalDate dateBookedTo;
    private ReservationType reservationType;
    private ReservationStatus reservationStatus;

    public Reservation(int reservationId,
                       int vehicleId,
                       int userId,
                       LocalDate dateBookedFrom,
                       LocalDate dateBookedTo,
                       ReservationType reservationType) {
        this.reservationId = reservationId;
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.dateBookedFrom = dateBookedFrom;
        this.dateBookedTo = dateBookedTo;
        this.reservationType = reservationType;
        this.reservationStatus = ReservationStatus.SCHEDULED;
    }
}
