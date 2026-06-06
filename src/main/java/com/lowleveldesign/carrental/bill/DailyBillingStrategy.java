package com.lowleveldesign.carrental.bill;

import com.lowleveldesign.carrental.entities.Vehicle;
import com.lowleveldesign.carrental.reservations.Reservation;
import com.lowleveldesign.carrental.vehiclemanager.VehicleInventoryManager;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DailyBillingStrategy implements BillingStrategy{

    VehicleInventoryManager vehicleInventoryManager;

    private final AtomicInteger billGenerator = new AtomicInteger(50000);

    public DailyBillingStrategy(VehicleInventoryManager vehicleInventoryManager){
        this.vehicleInventoryManager = vehicleInventoryManager;
    }

    @Override
    public Bill generateBill(Reservation reservation){

        long days = ChronoUnit.DAYS.between(reservation.getDateBookedFrom(), reservation.getDateBookedTo()) + 1;

        Vehicle vehicle = vehicleInventoryManager.getVehicle(reservation.getVehicleId()).get();
        double rate = vehicle.getDailyRentalCot();
        double total = days*rate;

        return new Bill(billGenerator.incrementAndGet(), reservation.getReservationId(), total);
        }

}
