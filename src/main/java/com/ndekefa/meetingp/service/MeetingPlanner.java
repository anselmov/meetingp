package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.entity.RoomEntity;

public interface MeetingPlanner {
    RoomEntity findRoom(MeetingDTO meetingDTO);
}
