package com.lowleveldesign.meetingscheduler.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {

    private int id;
    private String message;

    public void sendMeetingInviteNotification(User user, Meeting meeting) {
        System.out.println("[+] Invite Notification sent to " + user.getName() + " for meeting: " + meeting.getSubject());
    }

    public void sendCancelMeetingNotification(User user, Meeting meeting) {
        System.out.println("[-] Cancellation Notification sent to " + user.getName() + " for meeting: " + meeting.getSubject());
    }
}
