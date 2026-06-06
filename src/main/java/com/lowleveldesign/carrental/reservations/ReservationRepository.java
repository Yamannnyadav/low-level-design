package com.lowleveldesign.carrental.reservations;

import javax.management.openmbean.OpenDataException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReservationRepository {

    //reservationIds -> Reservation
    private final ConcurrentMap<Integer, Reservation> reservations;

    public ReservationRepository(){
        this.reservations = new ConcurrentHashMap<>();
    }

    //Save or Update reservations
    public void save(Reservation reservation){
        reservations.put(reservation.getReservationId(), reservation);
    }

    //Lookup reservation by id
    public Optional<Reservation> getReservationById(int id){
        return Optional.ofNullable(reservations.get(id));
    }

    //Delete reservation
    public void delete(int id){
        reservations.remove(id);
    }

    // Return all reservations (for reporting/debug)
    public ConcurrentMap<Integer, Reservation> getAll() {
        return reservations;
    }
}
