package com.lowleveldesign.meetingscheduler.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeInterval {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public boolean overlapsWith(TimeInterval newInterval) {
        // if the current interval ends before the new interval starts - valid: overlaps? false
        // if the current interval starts after the new interval ends - valid: overlaps? false
        return !this.endTime.isBefore(newInterval.startTime) &&
                !this.startTime.isAfter(newInterval.endTime);
    }
}
