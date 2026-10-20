package com.lowleveldesign.cabbookingsystem.strategy.ridematching;

import com.lowleveldesign.cabbookingsystem.enums.CabType;
import com.lowleveldesign.cabbookingsystem.enums.DriverStatus;
import com.lowleveldesign.cabbookingsystem.model.Driver;
import com.lowleveldesign.cabbookingsystem.model.Location;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NearestDriverMatchingStrategy implements DriverMatchingStrategy {

    private final double maxRadiusKm;

    public NearestDriverMatchingStrategy(double maxRadiusKm) {
        this.maxRadiusKm = maxRadiusKm;
    }

    @Override
    public List<Driver> findCandidates(Location pickup, CabType cabType, Collection<Driver> allDrivers) {
        return allDrivers.stream()
                .filter(d -> d.getStatus() == DriverStatus.AVAILABLE)
                .filter(d -> d.getCab().getCabType() == cabType)
                .filter(d -> d.getLocation().distanceTo(pickup) <= maxRadiusKm)
                .sorted(Comparator.comparingDouble(d -> d.getLocation().distanceTo(pickup)))
                .collect(Collectors.toList());
    }
}
