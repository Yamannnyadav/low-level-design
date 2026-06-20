package com.lowleveldesign.meetingscheduler.entities;

import com.lowleveldesign.meetingscheduler.enums.ParticipantResponse;
import com.lowleveldesign.meetingscheduler.enums.ParticipantType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class User {

    private String name;
    private String email;
    private Calendar calendar;
    private ParticipantType participantType;

    public User(String name, String email, ParticipantType participantType) {
        this.name = name;
        this.email = email;
        this.participantType = participantType;
        calendar = new Calendar();
    }

    public void viewCalendar(){
        System.out.println("\n===>>> Displaying " + this.name + "'s calendar for the day:");
        System.out.println(calendar);
    }

    public void respondToInvitation(User participant, Meeting meeting, ParticipantResponse participantResponse){
        meeting.getAttendees().put(participant, participantResponse);
        System.out.println("[+] " + participant.getName() + " responded: " + participantResponse);
        if(participantResponse == ParticipantResponse.DECLINED){
            this.calendar.removeMeeting(meeting);
        }
        else if(participantResponse == ParticipantResponse.ACCEPTED){
            this.calendar.addMeeting(meeting);
        }
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", email=" + email + ", calendar=" + calendar + ", participantType=" + participantType + "]";
    }
}
