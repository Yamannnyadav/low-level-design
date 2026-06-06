package com.lowleveldesign.carrental.vehiclemanager;

import com.lowleveldesign.carrental.entities.Vehicle;
import com.lowleveldesign.carrental.enums.VehicleStatus;
import com.lowleveldesign.carrental.enums.VehicleType;
import com.lowleveldesign.carrental.reservations.Reservation;
import com.lowleveldesign.carrental.reservations.ReservationRepository;
import com.lowleveldesign.carrental.utility.DateInterval;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class VehicleInventoryManager {

    // vehicleId → Vehicle
    private final ConcurrentMap<Integer, Vehicle> vehicles = new ConcurrentHashMap<>();

    // vehicleId → list of reservation IDs (metadata index)
    private final ConcurrentMap<Integer, List<Integer>> vehicleBookingIds = new ConcurrentHashMap<>();

    // per-vehicle lock
    private final ConcurrentMap<Integer, ReentrantLock> vehicleLocks = new ConcurrentHashMap<>();

    @Setter
    private ReservationRepository reservationRepository;

    public void addVehicle(Vehicle vehicle){
        vehicles.put(vehicle.getId(), vehicle);
    }

    public Optional<Vehicle> getVehicle(int vehicleId){
        return Optional.ofNullable(vehicles.get(vehicleId));
    }

    private ReentrantLock lockForVehicle(int vehicleId){
        vehicleLocks.putIfAbsent(vehicleId, new ReentrantLock());
        return vehicleLocks.get(vehicleId);
    }

    //VEHICLE AVAILABILITY CHECK
    public boolean isAvailable(int vehicleId, LocalDate from, LocalDate to) {

        Vehicle vehicle = vehicles.get(vehicleId);

        if(vehicle == null){
            return false;
        }

        if(vehicle.getStatus() == VehicleStatus.UNDER_MAINTENANCE){
            return false;
        }

        DateInterval requestedDateInterval = new DateInterval(from, to);

        List<Integer> reservationIds = vehicleBookingIds.get(vehicleId);
        if(reservationIds == null || reservationIds.isEmpty()){
            return true;
            //vehicle is available for rent.
        }

        for(int reservationId : reservationIds){

            Reservation reservation = reservationRepository.getReservationById(reservationId).get();
            LocalDate bookedFrom = reservation.getDateBookedFrom();
            LocalDate bookedTo = reservation.getDateBookedTo();
            DateInterval currDateInterval = new DateInterval(bookedFrom, bookedTo);

            if(currDateInterval.checkIfOverlaps(requestedDateInterval)){
                return false;
            }
        }

        return true;

    }


    //VEHICLE ATOMIC BOOKING
    public boolean reserve(int vehicleId, int reservationId, LocalDate from, LocalDate to) {

        ReentrantLock lock = new ReentrantLock();
        lock.lock();

        try{
            if(!isAvailable(vehicleId, from, to)){
                return false;
            }
            vehicleBookingIds.putIfAbsent(vehicleId, new ArrayList<>());
            vehicleBookingIds.get(vehicleId).add(reservationId);
            vehicles.get(vehicleId).setStatus(VehicleStatus.BOOKED);
            return true;
        }
        finally {
            lock.unlock();
        }

    }


    //ATOMIC RELEASE OF VEHICLE
    public void release(int vehicleId, int reservationId) {

        ReentrantLock lock = new ReentrantLock();
        lock.lock();

        try{
            //remove reservation id from the list.
            List<Integer> ids = vehicleBookingIds.get(vehicleId);
            if(ids != null){
                ids.remove(Integer.valueOf(reservationId));
            }

            //if no more bookings, mark vehicle AVAILABLE
            List<Integer> stillBookedIDs = vehicleBookingIds.get(vehicleId);
            if(stillBookedIDs == null || stillBookedIDs.isEmpty()){
                vehicles.get(vehicleId).setStatus(VehicleStatus.AVAILABLE);
            }
        }
        finally {
            lock.unlock();
        }

    }

    //SEARCH ALL VEHICLES
    public List<Vehicle> getAvailableVehicles(VehicleType type, LocalDate from, LocalDate to) {
        return vehicles.values().stream().
                filter(v -> v.getVehicleType() == type).
                filter(v -> isAvailable(v.getId(), from, to)).
                collect(Collectors.toList());


    }

}
