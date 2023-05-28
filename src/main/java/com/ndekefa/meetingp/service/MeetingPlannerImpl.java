package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.dto.RoomDTO;
import com.ndekefa.meetingp.data.repository.RoomRepository;
import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.Tool;
import com.ndekefa.meetingp.model.ToolType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

public class MeetingPlannerImpl implements MeetingPlanner {

    @Autowired
    private final RoomRepository roomRepository;

    public MeetingPlannerImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    List<Tool> findRequiredTools(MeetingType meetingType) {
        return switch (meetingType) {
            case VC ->
                    List.of(new Tool(ToolType.SCREEN), new Tool(ToolType.CONFERENCE_PHONE), new Tool(ToolType.WEBCAM));
            case SPEC -> List.of(new Tool(ToolType.WHITEBOARD));
            case RS -> Collections.emptyList();
            case RC ->
                    List.of(new Tool(ToolType.WHITEBOARD), new Tool(ToolType.SCREEN), new Tool(ToolType.CONFERENCE_PHONE));
        };
    }

    public RoomDTO findRoom(MeetingDTO meetingDTO) {
        int attendeesCount = meetingDTO.getAttendees();
        MeetingType meetingType = meetingDTO.getType();

        return roomRepository.findAll().stream()
                .filter(byCapacity(attendeesCount))
                .filter(byTools(meetingType))
                .min(Comparator.comparingInt(RoomDTO::getCapacity))
                .orElseThrow();

    }

    final Predicate<RoomDTO> byCapacity(int attendeesCount) {
        return room -> (int) (room.getCapacity() * 0.7) >= attendeesCount;
    }

    Predicate<RoomDTO> byTools(MeetingType meetingType) {
        List<Tool> requiredTools = findRequiredTools(meetingType);
        return requiredTools.isEmpty() ? (x) -> true :
                room -> new HashSet<>(room.getTools()).containsAll(requiredTools);
    }
}
