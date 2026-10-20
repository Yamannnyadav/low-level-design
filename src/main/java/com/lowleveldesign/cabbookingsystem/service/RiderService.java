package com.lowleveldesign.cabbookingsystem.service;

import com.lowleveldesign.cabbookingsystem.exceptions.ResourceNotFoundException;
import com.lowleveldesign.cabbookingsystem.model.Rider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RiderService {

    private final ConcurrentHashMap<String, Rider> riders = new ConcurrentHashMap<>(); //riderId vs Rider
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public Rider registerRider(String name, String phone) {
        Rider rider = new Rider("R-" + idCounter.getAndIncrement(), name, phone);
        riders.put(rider.getId(), rider);
        return rider;
    }

    public Rider getRider(String riderId) {
        Rider rider = riders.get(riderId);
        if (rider == null) {
            throw new ResourceNotFoundException("Rider not found: " + riderId);
        }
        return rider;
    }
}
