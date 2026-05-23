package com.lowleveldesign.parkinglot.parkingclient;

import com.lowleveldesign.parkinglot.enumclasses.VehicleType;
import com.lowleveldesign.parkinglot.model.ParkingSpot;
import com.lowleveldesign.parkinglot.model.Ticket;
import com.lowleveldesign.parkinglot.model.Vehicle;
import com.lowleveldesign.parkinglot.parkinglookupstrategy.ParkingLookupStrategy;
import com.lowleveldesign.parkinglot.parkinglookupstrategy.RandomLookupStrategy;
import com.lowleveldesign.parkinglot.parkinglot.*;
import com.lowleveldesign.parkinglot.parkingspotmanagers.FourWheelerSpotManager;
import com.lowleveldesign.parkinglot.parkingspotmanagers.ParkingSpotManager;
import com.lowleveldesign.parkinglot.parkingspotmanagers.TwoWheelerSpotManager;
import com.lowleveldesign.parkinglot.payment.CashPayment;
import com.lowleveldesign.parkinglot.payment.UPIPayment;
import com.lowleveldesign.parkinglot.pricing.CostComputation;
import com.lowleveldesign.parkinglot.pricing.FixedPricingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingClient {

    public static void main(String[] args){

        ParkingLookupStrategy parkingLookupStrategy = new RandomLookupStrategy();

        List<ParkingSpot> twoWheeelerParkingSpotList = new ArrayList<>();
        twoWheeelerParkingSpotList.add(new ParkingSpot(101));
        twoWheeelerParkingSpotList.add(new ParkingSpot(102));
        Map<VehicleType, ParkingSpotManager> levelOneManager = new HashMap<>();
        levelOneManager.put(VehicleType.BIKE, new TwoWheelerSpotManager(twoWheeelerParkingSpotList, parkingLookupStrategy));

        List<ParkingSpot> fourWheeelerParkingSpotList = new ArrayList<>();
        fourWheeelerParkingSpotList.add(new ParkingSpot(120));
        fourWheeelerParkingSpotList.add(new ParkingSpot(121));
        levelOneManager.put(VehicleType.CAR, new FourWheelerSpotManager(fourWheeelerParkingSpotList, parkingLookupStrategy));

        ParkingLevel parkingLevel1 = new ParkingLevel(1, levelOneManager);

        twoWheeelerParkingSpotList.clear();
        fourWheeelerParkingSpotList.clear();

        twoWheeelerParkingSpotList.add(new ParkingSpot(201));
        twoWheeelerParkingSpotList.add(new ParkingSpot(202));
        Map<VehicleType, ParkingSpotManager> levelTwoManager = new HashMap<>();
        levelOneManager.put(VehicleType.BIKE, new TwoWheelerSpotManager(twoWheeelerParkingSpotList, parkingLookupStrategy));

        fourWheeelerParkingSpotList.add(new ParkingSpot(221));
        fourWheeelerParkingSpotList.add(new ParkingSpot(222));
        levelOneManager.put(VehicleType.CAR, new FourWheelerSpotManager(fourWheeelerParkingSpotList, parkingLookupStrategy));

        ParkingLevel parkingLevel2 = new ParkingLevel(2, levelTwoManager);

        ParkingBuilding parkingBuilding = new ParkingBuilding(List.of(parkingLevel1, parkingLevel2), new CostComputation(new FixedPricingStrategy()));

        ParkingLot parkingLot = new ParkingLot(parkingBuilding, new EntranceGate(), new ExitGate(new CostComputation(new FixedPricingStrategy())));

        Vehicle bike = new Vehicle("HR 36 47T 2122", VehicleType.BIKE);
        Vehicle car = new Vehicle("HR 26 55T 9296", VehicleType.CAR);

        Ticket ticket1 =  parkingLot.vehicleArrives(bike);
        Ticket ticket2 =  parkingLot.vehicleArrives(car);

        parkingLot.vehicleExits(ticket1, new CashPayment());
        parkingLot.vehicleExits(ticket2, new UPIPayment());


    }

}
