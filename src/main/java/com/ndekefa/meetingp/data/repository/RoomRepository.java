package com.ndekefa.meetingp.data.repository;

import com.ndekefa.meetingp.data.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> findAll();
}
