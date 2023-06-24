package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.entity.RoomEntity;

import java.util.List;
import java.util.Optional;

public interface MeetingPlanner {
    Optional<RoomEntity> schedule(MeetingDTO meetingDTO);
}
