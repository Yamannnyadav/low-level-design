package com.lowleveldesign.meetingscheduler.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class Calendar {

    private Map<TimeInterval, Meeting> meetings;

    public Calendar() {
        this.meetings = new HashMap<>();
    }

    public void addMeeting(Meeting meeting){
        meetings.put(meeting.getTimeInterval(), meeting);
    }

    public void removeMeeting(Meeting meeting){
        meetings.remove(meeting.getTimeInterval());
    }

    public List<Meeting> getMeetings(){
        return new ArrayList<>(this.meetings.values());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Meeting m : meetings.values())
            builder.append(m.toString().concat("\n"));
        return builder.toString();
    }

}
