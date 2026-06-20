package com.lowleveldesign.meetingscheduler.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MeetingRoom {

    private String name;
    private int id;
    private int capacity;
    private List<TimeInterval> bookedSlots;
    private Calendar calendar;

    public MeetingRoom(int id, String name, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.name = name;
        this.bookedSlots = new ArrayList<>();
        this.calendar = new Calendar();
    }

    public void viewCalendar() {
        System.out.println("\n===>>> Displaying Meeting " + this.name + "'s calendar for the day:");
        System.out.println(calendar);
    }

    public void addBookedSlot(TimeInterval bookedSlot) {
        bookedSlots.add(bookedSlot);
    }

    public void removeBookedSlot(TimeInterval bookedSlot) {
        bookedSlots.remove(bookedSlot);
    }

    public boolean isAvailable(TimeInterval interval, int reqCapacity){
        if(reqCapacity > this.capacity){
            return false;
        }
        for(TimeInterval timeInterval : this.bookedSlots){
            if(timeInterval.overlapsWith(interval)){
                return false;
            }
        }
        return true;
    }


}
