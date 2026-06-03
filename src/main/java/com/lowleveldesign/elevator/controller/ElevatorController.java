package com.lowleveldesign.elevator.controller;

import com.lowleveldesign.elevator.enums.ElevatorDirection;
import com.lowleveldesign.elevator.model.ElevatorCar;

import java.util.concurrent.PriorityBlockingQueue;

public class ElevatorController implements Runnable{

    PriorityBlockingQueue<Integer> minUpPQ;
    PriorityBlockingQueue<Integer> maxDownPQ;
    ElevatorCar elevatorCar;

    private final Object monitor = new Object();

    public ElevatorController(ElevatorCar elevatorCar){
        this.elevatorCar = elevatorCar;
        minUpPQ = new PriorityBlockingQueue<>();
        maxDownPQ = new PriorityBlockingQueue<>(10, (a,b) -> b - a);
    }

    public void submitRequest(int destFloor){
        enqueueRequest(destFloor);
    }

    private void enqueueRequest(int destFloor){

        System.out.println("Request details => destination floor: " + destFloor + " accepted by elevator: " + elevatorCar.getElevatorId());

        if(destFloor == elevatorCar.getNextFloorStoppage()){
            return;
        }
        else if(destFloor > elevatorCar.getNextFloorStoppage()){
            if(!minUpPQ.contains(destFloor)){
                minUpPQ.offer(destFloor);
            }
        }
        else{
            if(!maxDownPQ.contains(destFloor)){
                maxDownPQ.offer(destFloor);
            }
        }

        synchronized (monitor) {
            monitor.notify();
        }

    }

    @Override
    public void run(){
        controlElevator();
    }

    private void controlElevator(){

        while (true) {

            synchronized (monitor){
                while(minUpPQ.isEmpty() && maxDownPQ.isEmpty()){
                    try{
                        System.out.println("elevator: " + elevatorCar.getElevatorId() + " is idle");
                        elevatorCar.setElevatorDirection(ElevatorDirection.IDLE);
                        monitor.wait();
                    } catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }
            }

            while(!minUpPQ.isEmpty()){
                int floor = minUpPQ.poll();
                System.out.println("Serving floor: " + floor + " by elevator:" + elevatorCar.getElevatorId() + " currentFloor: " + elevatorCar.getCurrFloor());
                elevatorCar.moveElevator(floor);
            }

            while(!maxDownPQ.isEmpty()){
                int floor = maxDownPQ.poll();
                System.out.println("Serving floor: " + floor + " by elevator:" + elevatorCar.getElevatorId() + " currentFloor: " + elevatorCar.getCurrFloor());
                elevatorCar.moveElevator(floor);
            }

        }

    }

    public ElevatorCar getElevatorCar() {
        return elevatorCar;
    }

    public PriorityBlockingQueue<Integer> getMinUpPQ() {
        return minUpPQ;
    }

    public PriorityBlockingQueue<Integer> getMaxDownPQ() {
        return maxDownPQ;
    }
}
