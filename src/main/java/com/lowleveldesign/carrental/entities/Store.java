package com.lowleveldesign.carrental.entities;

import com.lowleveldesign.carrental.bill.Bill;
import com.lowleveldesign.carrental.bill.BillManager;
import com.lowleveldesign.carrental.bill.BillingStrategy;
import com.lowleveldesign.carrental.bill.DailyBillingStrategy;
import com.lowleveldesign.carrental.enums.ReservationType;
import com.lowleveldesign.carrental.enums.VehicleType;
import com.lowleveldesign.carrental.payments.Payment;
import com.lowleveldesign.carrental.payments.PaymentManager;
import com.lowleveldesign.carrental.payments.PaymentStrategy;
import com.lowleveldesign.carrental.payments.UPIPaymentStrategy;
import com.lowleveldesign.carrental.reservations.Reservation;
import com.lowleveldesign.carrental.reservations.ReservationManager;
import com.lowleveldesign.carrental.vehiclemanager.VehicleInventoryManager;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class Store {

    @Getter
    private final int storeId;
    private final Location storeLocation;
    @Getter
    private final VehicleInventoryManager inventory;
    private final ReservationManager reservationManager;

    private final BillManager billManager;
    private final PaymentManager paymentManager;

    public Store(int storeId, Location location) {

        this.storeId = storeId;
        this.storeLocation = location;
        this.inventory = new VehicleInventoryManager();
        this.billManager = new BillManager(new DailyBillingStrategy(inventory)); //default
        this.paymentManager = new PaymentManager(new UPIPaymentStrategy()); //default
        this.reservationManager = new ReservationManager(inventory);
    }

    // ----------------- Search Vehicles --------------------

    public List<Vehicle> getVehicles(VehicleType type, LocalDate from, LocalDate to) {
        return inventory.getAvailableVehicles(type, from, to);
    }

    // ----------------- Create Reservation -----------------
    public Reservation createReservation(int vehicleId, User user, LocalDate from, LocalDate to,
                                         ReservationType type) throws Exception {
        return reservationManager.createReservation(vehicleId, user, from, to, type);
    }

    // ----------------- Update Reservation -----------------

    public void cancelReservation(int reservationId) {
        reservationManager.cancelReservation(reservationId);
    }

    public void startTrip(int reservationId) {
        reservationManager.startTrip(reservationId);
    }

    public void submitVehicle(int reservationId) {
        reservationManager.submitVehicle(reservationId);
    }

    // ----------------- Billing & Payment ------------------

    public Bill generateBill(int reservationId, BillingStrategy billingStrategy) {
        Reservation r = reservationManager.findByID(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        billManager.setBillingStrategy(billingStrategy);
        return billManager.generateBill(r);
    }

    public Payment makePayment(Bill bill, PaymentStrategy paymentStrategy, double paymentAmount) {

        paymentManager.setPaymentStrategy(paymentStrategy);
        Payment payment = paymentManager.makePayment(bill, paymentAmount);

        if (!bill.isBillPaid()) {
            throw new RuntimeException("Payment failed");
        }

        // NOW we can safely remove the reservation from the repo
        reservationManager.remove(bill.getReservationId());
        return payment;
    }


}
