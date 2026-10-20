package com.lowleveldesign.cabbookingsystem.observer;

import com.lowleveldesign.cabbookingsystem.model.Ride;

public interface RideObserver {
    void onRideUpdate(Ride ride);
}
