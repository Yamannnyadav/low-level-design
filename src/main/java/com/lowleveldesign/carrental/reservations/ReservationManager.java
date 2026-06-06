package com.lowleveldesign.carrental.reservations;

import com.lowleveldesign.carrental.entities.User;
import com.lowleveldesign.carrental.enums.ReservationStatus;
import com.lowleveldesign.carrental.enums.ReservationType;
import com.lowleveldesign.carrental.vehiclemanager.VehicleInventoryManager;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ReservationManager {

    private final VehicleInventoryManager vehicleInventory;
    private final ReservationRepository reservationRepository;
    private AtomicInteger reservationIdGenerator = new AtomicInteger(20000);

    public ReservationManager(VehicleInventoryManager vehicleInventory){
        this.vehicleInventory = vehicleInventory;
        this.reservationRepository = new ReservationRepository();
        this.vehicleInventory.setReservationRepository(this.reservationRepository);
    }

    //GET RESERVATION
    public Optional<Reservation> findByID(int reservationId){
        return reservationRepository.getReservationById(reservationId);
    }

    //CREATE RESERVATION
    public Reservation createReservation(int vehicleId, User user, LocalDate from, LocalDate to, ReservationType type){

        int reservationId = reservationIdGenerator.getAndIncrement();
        boolean reserved = vehicleInventory.reserve(vehicleId, reservationId, from, to);

        if(!reserved){
            throw new RuntimeException("Vehicle not available for selected dates");
        }

        Reservation reservation = new Reservation(reservationId, vehicleId, user.getId(), from, to, type);
        reservationRepository.save(reservation);
        return reservation;
    }

    //CANCEL RESERVATION
    public void cancelReservation(int reservationId){

        Optional<Reservation> reservation = reservationRepository.getReservationById(reservationId);
        if(!reservation.isPresent()){
            throw new RuntimeException("Reservation not found");
        }

        Reservation r = reservation.get();
        r.setReservationStatus(ReservationStatus.CANCELLED);
        vehicleInventory.release(r.getVehicleId(), r.getReservationId());
        reservationRepository.delete(reservationId);
    }

    //START TRIP
    public void startTrip(int reservationId){
        Optional<Reservation> reservation = reservationRepository.getReservationById(reservationId);
        if(!reservation.isPresent()){
            throw new RuntimeException("Reservation not found");
        }

        Reservation r = reservation.get();
        r.setReservationStatus(ReservationStatus.IN_USE);
    }

    //SUBMIT VEHICLE
    public void submitVehicle(int reservationId){
        Optional<Reservation> reservation = reservationRepository.getReservationById(reservationId);
        if(!reservation.isPresent()){
            throw new RuntimeException("Reservation not found");
        }

        Reservation r = reservation.get();
        r.setReservationStatus(ReservationStatus.COMPLETED);
        vehicleInventory.release(r.getVehicleId(), r.getReservationId());
    }

    //REMOVE RESERVATION
    public void remove(int reservationId) {
        reservationRepository.delete(reservationId);
    }
}
