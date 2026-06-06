package com.lowleveldesign.carrental.execute;

import com.lowleveldesign.carrental.bill.Bill;
import com.lowleveldesign.carrental.bill.DailyBillingStrategy;
import com.lowleveldesign.carrental.entities.Location;
import com.lowleveldesign.carrental.entities.Store;
import com.lowleveldesign.carrental.entities.User;
import com.lowleveldesign.carrental.entities.Vehicle;
import com.lowleveldesign.carrental.enums.ReservationType;
import com.lowleveldesign.carrental.enums.VehicleType;
import com.lowleveldesign.carrental.payments.Payment;
import com.lowleveldesign.carrental.payments.UPIPaymentStrategy;
import com.lowleveldesign.carrental.reservations.Reservation;
import com.lowleveldesign.carrental.vehiclemanager.VehicleRentalSystem;

import java.time.LocalDate;

public class ExecuteCarRentalSystem {

    public static void main(String[] args) throws Exception {

        System.out.println("\n===== LLD: Car Rental System Demo =====");

        VehicleRentalSystem vehicleRentalSystem = new VehicleRentalSystem();

        // ---------------------------------------------------------
        // 1. Create Stores in System
        // ---------------------------------------------------------

        //Create a store1
        Location store1Location = new Location(
                45,
                "Area1",
                "City1",
                "State1",
                "India",
                12345
        );
        Store store1 = new Store(1001, store1Location);
        vehicleRentalSystem.addStore(store1);

        // ---------------------------------------------------------
        // 2. Create Users in System
        // ---------------------------------------------------------

        //create 2 users here
        User user1 = new User("Alex", 801, "DL2022GDG556690");
        User user2 = new User("Costa", 802, "DL2017DHW9090765231");
        vehicleRentalSystem.addUser(user1);
        vehicleRentalSystem.addUser(user2);

        // ---------------------------------------------------------
        // 3. Add vehicles to store inventory
        // ---------------------------------------------------------

        Vehicle v1 = new Vehicle("DL1234", 1, VehicleType.FOUR_WHEELER, 1100);
        Vehicle v2 = new Vehicle("DL5678",2,  VehicleType.FOUR_WHEELER, 1400);

        store1.getInventory().addVehicle(v1);
        store1.getInventory().addVehicle(v2);

        // ---------------------------------------------------------
        // 4. User selects store and searches vehicles
        // ---------------------------------------------------------
        Store selectedStore = vehicleRentalSystem.getStore(1001);

        LocalDate fromDate = LocalDate.of(2026, 12, 5);
        LocalDate toDate   = LocalDate.of(2026, 12, 7);

        System.out.println("\nAvailable vehicles from " + fromDate + " to " + toDate + ":");

        for (Vehicle v : selectedStore.getVehicles(VehicleType.FOUR_WHEELER, fromDate, toDate)) {
            System.out.println(" - " + v.getId() + ": " + v.getVehicleType());
        }

        // ---------------------------------------------------------
        // 5. User creates reservation
        // ---------------------------------------------------------
        System.out.println("\nCreating reservation...");

        Reservation reservation =
                selectedStore.createReservation(
                        1,
                        user1,
                        fromDate,
                        toDate,
                        ReservationType.DAILY
                );

        System.out.println("Reservation created with ID: " + reservation.getReservationId());


        // ---------------------------------------------------------
        // 6. User starts the trip
        // ---------------------------------------------------------
        System.out.println("\nStarting trip...");
        selectedStore.startTrip(reservation.getReservationId());


        // ---------------------------------------------------------
        // 7. User submits the vehicle
        // ---------------------------------------------------------
        System.out.println("Submitting vehicle...");
        selectedStore.submitVehicle(reservation.getReservationId());


        // ---------------------------------------------------------
        // 8. System generates the bill
        // ---------------------------------------------------------
        System.out.println("\nGenerating bill...");
        Bill bill = selectedStore.generateBill(reservation.getReservationId(),
                new DailyBillingStrategy(selectedStore.getInventory()));

        System.out.println("Bill ID: " + bill.getId());
        System.out.println("Bill Amount: " + bill.getTotalBillAmount());


        // ---------------------------------------------------------
        // 8. User makes payment
        // ---------------------------------------------------------
        System.out.println("\nProcessing Payment...");

        Payment payment = selectedStore.makePayment(bill, new UPIPaymentStrategy(), bill.getTotalBillAmount());

        System.out.println("\n===== PAYMENT RECEIPT =====");
        System.out.println("Payment ID: " + payment.getPaymentId());
        System.out.println("Paid Amount: " + payment.getAmountPaid());
        System.out.println("Payment Mode: " + payment.getPaymentMode());
        System.out.println("Payment Date: " + payment.getPaymentDate());
        System.out.println("============================");
    }



}
