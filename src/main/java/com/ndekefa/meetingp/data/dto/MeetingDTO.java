package com.ndekefa.meetingp.data.dto;

import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.Room;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class MeetingDTO {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private MeetingType type;
    private int attendees;
    private boolean scheduled;
}
