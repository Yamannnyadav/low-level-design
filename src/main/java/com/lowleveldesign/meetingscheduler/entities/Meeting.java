package com.lowleveldesign.meetingscheduler.entities;

import com.lowleveldesign.meetingscheduler.enums.ParticipantResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {

    private String id;
    private String subject;
    private String desc;
    private User organiser;
    private TimeInterval timeInterval;
    private Map<User, ParticipantResponse> attendees;
    private MeetingRoom meetingRoom;

    public List<User> getAcceptedParticipants(){
        List<User> acceptedParticipants = new ArrayList<>();
        for(Map.Entry<User, ParticipantResponse> entry : attendees.entrySet()){
            if(entry.getValue() == ParticipantResponse.ACCEPTED){
                acceptedParticipants.add(entry.getKey());
            }
        }
        return acceptedParticipants;
    }

    public List<User> getDeclinedParticipants(){
        List<User> declinedParticipants = new ArrayList<>();
        for(Map.Entry<User, ParticipantResponse> entry : attendees.entrySet()){
            if(entry.getValue() == ParticipantResponse.ACCEPTED){
                declinedParticipants.add(entry.getKey());
            }
        }
        return declinedParticipants;
    }

    public List<User> getTentativeParticipants(){
        List<User> tentativeParticipants = new ArrayList<>();
        for(Map.Entry<User, ParticipantResponse> entry : attendees.entrySet()){
            if(entry.getValue() == ParticipantResponse.ACCEPTED){
                tentativeParticipants.add(entry.getKey());
            }
        }
        return tentativeParticipants;
    }

    @Override
    public String toString() {
        return "Meeting [meetingId=" + id + ", subject=" + subject + ", organizer=" + organiser.getName() + ", meetingRoom=" + meetingRoom.getName() + "]";
    }

}
