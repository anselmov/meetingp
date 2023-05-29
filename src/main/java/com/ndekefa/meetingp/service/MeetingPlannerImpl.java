package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.dto.RoomDTO;
import com.ndekefa.meetingp.data.entity.RoomEntity;
import com.ndekefa.meetingp.data.repository.RoomRepository;
import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

@Service
public class MeetingPlannerImpl implements MeetingPlanner {

    @Autowired
    private final RoomRepository roomRepository;

    @Autowired
    private final ToolService toolService;

    public MeetingPlannerImpl(RoomRepository roomRepository, ToolService toolService) {
        this.roomRepository = roomRepository;
        this.toolService = toolService;
    }

    public RoomEntity findRoom(MeetingDTO meetingDTO) {
        int attendeesCount = meetingDTO.getAttendees();
        MeetingType meetingType = meetingDTO.getType();
        List<RoomEntity> allRooms = roomRepository.findAll();
        Optional<RoomEntity> min = allRooms.stream()
                .filter(byCapacity(attendeesCount))
                .peek(System.out::println)
                .filter(roomDTO -> toolService.hasRequiredTools(roomDTO.getTools(), meetingType))
                .min(Comparator.comparingInt(RoomEntity::getCapacity));
        if (min.isPresent()) {
            return min.get();
        } else {
            RoomEntity roomDTO1 = allRooms.stream()
                    .filter(byCapacity(attendeesCount))
                    .min(Comparator.comparingInt(RoomEntity::getCapacity))
                    .get();
            roomDTO1.setTools(toolService.findRequiredTools(roomDTO1.getTools(), meetingType));
            return roomDTO1;
        }
    }

    final Predicate<RoomEntity> byCapacity(int attendeesCount) {
        return room -> (int) (room.getCapacity() * 0.7) >= attendeesCount;
    }
}
