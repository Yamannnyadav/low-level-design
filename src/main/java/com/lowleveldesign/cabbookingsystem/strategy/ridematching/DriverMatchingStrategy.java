package com.lowleveldesign.cabbookingsystem.strategy.ridematching;

import com.lowleveldesign.cabbookingsystem.enums.CabType;
import com.lowleveldesign.cabbookingsystem.model.Driver;
import com.lowleveldesign.cabbookingsystem.model.Location;

import java.util.Collection;
import java.util.List;

public interface DriverMatchingStrategy {
    List<Driver> findCandidates(Location pickup, CabType cabType, Collection<Driver> allDrivers);
}
