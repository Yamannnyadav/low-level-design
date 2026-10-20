package com.lowleveldesign.cabbookingsystem.observer;

import com.lowleveldesign.cabbookingsystem.model.Driver;
import com.lowleveldesign.cabbookingsystem.model.Ride;

public class ConsoleNotificationObserver implements RideObserver {

    @Override
    public void onRideUpdate(Ride ride) {
        Driver driver = ride.getDriver();
        String driverInfo = driver == null
                ? ""
                : " | Driver " + driver.getName() + " (" + driver.getCab().getLicensePlate() + ")";
        System.out.println("[NOTIFY] Rider " + ride.getRider().getName()
                + ": " + ride.getId() + " -> " + ride.getStatus() + driverInfo);
    }
}
