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

        /*
         * =========================================================
         * LEVEL 1 CREATION
         * =========================================================
         */
        List<ParkingSpot> level1TwoWheelerSpots = new ArrayList<>();
        level1TwoWheelerSpots.add(new ParkingSpot(101));
        level1TwoWheelerSpots.add(new ParkingSpot(102));

        List<ParkingSpot> level1FourWheelerSpots = new ArrayList<>();
        level1FourWheelerSpots.add(new ParkingSpot(120));
        level1FourWheelerSpots.add(new ParkingSpot(121));

        Map<VehicleType, ParkingSpotManager> levelOneManager = new HashMap<>();

        levelOneManager.put(VehicleType.BIKE, new TwoWheelerSpotManager(level1TwoWheelerSpots, parkingLookupStrategy));

        levelOneManager.put(VehicleType.CAR, new FourWheelerSpotManager(level1FourWheelerSpots, parkingLookupStrategy));

        ParkingLevel parkingLevel1 = new ParkingLevel(1, levelOneManager);


        /*
         * =========================================================
         * LEVEL 2 CREATION
         * =========================================================
         */

        List<ParkingSpot> level2TwoWheelerSpots = new ArrayList<>();
        level2TwoWheelerSpots.add(new ParkingSpot(201));
        level2TwoWheelerSpots.add(new ParkingSpot(202));

        List<ParkingSpot> level2FourWheelerSpots = new ArrayList<>();
        level2FourWheelerSpots.add(new ParkingSpot(221));
        level2FourWheelerSpots.add(new ParkingSpot(222));

        Map<VehicleType, ParkingSpotManager> levelTwoManager = new HashMap<>();

        levelTwoManager.put(VehicleType.BIKE, new TwoWheelerSpotManager(level2TwoWheelerSpots, parkingLookupStrategy));

        levelTwoManager.put(VehicleType.CAR, new FourWheelerSpotManager(level2FourWheelerSpots, parkingLookupStrategy));

        ParkingLevel parkingLevel2 = new ParkingLevel(2, levelTwoManager);


        /*
         * =========================================================
         * PARKING BUILDING
         * =========================================================
         */

        ParkingBuilding parkingBuilding = new ParkingBuilding(List.of(parkingLevel1, parkingLevel2), new CostComputation(new FixedPricingStrategy())
        );


        /*
         * =========================================================
         * PARKING LOT
         * =========================================================
         */

        ParkingLot parkingLot = new ParkingLot(parkingBuilding, new EntranceGate(), new ExitGate(new CostComputation(new FixedPricingStrategy())));


        /*
         * =========================================================
         * VEHICLES
         * =========================================================
         */

        Vehicle bike = new Vehicle("HR 36 47T 2122", VehicleType.BIKE);

        Vehicle car = new Vehicle("HR 26 55T 9296", VehicleType.CAR);


        /*
         * =========================================================
         * VEHICLE ENTRY
         * =========================================================
         */

        Ticket ticket1 = parkingLot.vehicleArrives(bike);
        Ticket ticket2 = parkingLot.vehicleArrives(car);


        /*
         * =========================================================
         * VEHICLE EXIT
         * =========================================================
         */

        parkingLot.vehicleExits(ticket1, new CashPayment());
        parkingLot.vehicleExits(ticket2, new UPIPayment());

    }
}