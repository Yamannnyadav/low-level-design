package com.lowleveldesign.meetingscheduler.processor;

import com.lowleveldesign.meetingscheduler.entities.*;
import com.lowleveldesign.meetingscheduler.enums.ParticipantResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class MeetingScheduler {

    private List<MeetingRoom> meetingRoomList;

    public Meeting scheduleMeeting(User organiser, Map<User, ParticipantResponse> participants, String subject, TimeInterval timeInterval) {

        //check for available meeting rooms for given time slot
        MeetingRoom room = checkForAvailableRooms(participants.size(), timeInterval);
        if (room == null) {
            System.out.println("===>>> No available meeting room");
            return null;
        }

        System.out.println("\n===>>> Available meeting room found");

        // Create meeting
        Meeting meeting = new Meeting(getMeetingId(), subject, "Dummy Meeting Desc", organiser, timeInterval, participants, room);

        // Add meeting to participants(both organiser and participants) calendar
        organiser.getCalendar().addMeeting(meeting);
        for(Map.Entry<User, ParticipantResponse> entry : participants.entrySet()){
            entry.getKey().getCalendar().addMeeting(meeting);
        }

        // Add meeting to meeting room calendar
        bookSlot(room, meeting);

        // Notify participants
        notifyParticipantsWithInvites(meeting);
        simulateUserResponsesForInvites(meeting);
        System.out.println("[+] Meeting scheduled successfully!!");

        return meeting;

    }

    public void cancelMeeting(Meeting meeting){
        System.out.println("\n===>>> Cancelling meeting: " + meeting.getId() + " - " + meeting.getSubject());
        releaseSlot(meeting.getMeetingRoom(), meeting);
        //notify participants
        notifyParticipantsAboutMeetingCancellation(meeting);
    }

    private void releaseSlot(MeetingRoom room, Meeting meeting) {
        room.removeBookedSlot(meeting.getTimeInterval());
        room.getCalendar().removeMeeting(meeting);
    }

    private void notifyParticipantsAboutMeetingCancellation(Meeting meeting) {
        // Send notification and Remove meeting from participants calendar from accepted participants calendar
        Notifications notification = new Notifications(2, "Meeting Cancelled: " + meeting.getId() + " - " + meeting.getSubject());
        meeting.getOrganiser().getCalendar().removeMeeting(meeting);
        for (User user : meeting.getAcceptedParticipants()) {
            user.getCalendar().removeMeeting(meeting);
            notification.sendCancelMeetingNotification(user, meeting);
        }
    }

    private MeetingRoom checkForAvailableRooms(int capacity, TimeInterval timeInterval){

        for(MeetingRoom room : meetingRoomList){
            if(room.isAvailable(timeInterval, capacity)){
                return room;
            }
        }

        return null;

    }

    private String getMeetingId(){
        String str = String.valueOf(System.currentTimeMillis());
        return "MTS-" + str.substring(9, 13);
    }

    private void bookSlot(MeetingRoom room, Meeting meeting) {
        room.addBookedSlot(meeting.getTimeInterval());
        room.getCalendar().addMeeting(meeting);
    }

    private void notifyParticipantsWithInvites(Meeting meeting){
        Notifications notification = new Notifications(1, "Meeting Invitation: " + meeting.getSubject());
        meeting.getOrganiser().getCalendar().addMeeting(meeting);
        for(User user : meeting.getAttendees().keySet()){
            user.getCalendar().addMeeting(meeting);
            notification.sendMeetingInviteNotification(user, meeting);
        }
    }

    private void simulateUserResponsesForInvites(Meeting meeting) {
        for (User user : meeting.getAttendees().keySet()) {
            user.respondToInvitation(user, meeting, ParticipantResponse.ACCEPTED);
        }
    }

    public void addMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRoomList.add(meetingRoom);
    }

    public void removeMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRoomList.remove(meetingRoom);
    }
}
