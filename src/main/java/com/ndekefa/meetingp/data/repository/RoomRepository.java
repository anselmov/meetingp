package com.ndekefa.meetingp.data.repository;

import com.ndekefa.meetingp.data.dto.RoomDTO;

import java.util.List;

public interface RoomRepository {
    List<RoomDTO> findAll();
}
