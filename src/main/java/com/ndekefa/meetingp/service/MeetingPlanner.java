package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.dto.RoomDTO;

public interface MeetingPlanner {
    RoomDTO findRoom(MeetingDTO meetingDTO);
}
