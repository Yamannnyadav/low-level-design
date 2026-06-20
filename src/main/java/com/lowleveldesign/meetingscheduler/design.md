`# MEETING SCHEDULER

**Problem Statement**:
Design a detailed low-level-design of Meeting Scheduler where user can schedule a meeting.

**Functional Requirements**:
- User should be able to schedule a meeting by specifying:
    - Title
    - Description
    - Start time
    - End time
    - List of participants
- System should validate participant availability before scheduling a meeting.
- User should be able to invite one or more participants to a meeting.
- Participants should be able to accept, reject, or tentatively respond to meeting invitations.
- User should be able to modify meeting details such as:
    - Time
    - Participants
    - Description
- System should notify participants whenever a meeting is created or modified.
- User should be able to cancel a meeting.
- System should maintain the status of each invitation (Pending, Accepted, Rejected, Tentative).
- System should prevent scheduling conflicts for the same participant during overlapping time slots.
- User should be able to view all upcoming meetings.


**Non-Function Requirements**:
- System should support thousands of concurrent meeting scheduling requests.
- Scheduling conflicts should be prevented.
- Notifications should be delivered asynchronously.


**Core Entities**:
- User
- Meeting
- Calender
- MeetingRoom
- Notifications
- TimeInterval


**Entities and their relationship:**

User
- name
- email
- Calender calender
- ParticipantType participant


ParticipantType
- enum: ORGANISER, PARTICIPANT


Meeting
- id
- subject
- User organiser
- TimeInterval timeInterval
- Desc
- MeetingRoom
- Map<User, ParticipantResponse> attendees


TimeInterval
- startTime
- endTime


ParticipantResponse
- ACCEPTED
- DECLINED
- TENTATIVE
- NOT_RESPONDED


Calender
- Map<TimeInterval, Meeting> meetings


MeetingRoom
- id
- name
- capacity
- List<TimeInterval> bookedSlots
- Calender calender


Notifications
- id
- message