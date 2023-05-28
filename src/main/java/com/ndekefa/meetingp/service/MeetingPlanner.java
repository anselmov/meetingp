package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.dto.RoomDTO;
import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.Tool;
import com.ndekefa.meetingp.model.ToolType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MeetingPlanner {

    private List<RoomDTO> rooms;
    private List<Tool> movableTools;
    private List<MeetingDTO> meetingInputs;

    public List<RoomDTO> findByCapacity(int attendeesCount) {
        return rooms.stream()
                .filter(room -> (int) (room.getCapacity() * 0.7) >= attendeesCount)
                .toList();
    }

    /**
     * find the most suitable room with the minimal and limited capacity
     *
     * @param attendeesCount
     * @return RoomDTO || NoSuchElementException
     */
    RoomDTO findByEfficientCapacity(int attendeesCount) {
        return rooms.stream()
                .filter(room -> (int) (room.getCapacity() * 0.7) >= attendeesCount)
                .min(Comparator.comparingInt(RoomDTO::getCapacity))
                .orElseThrow();
    }


    public void setFixtures(List<RoomDTO> rooms, List<Tool> movableTools, List<MeetingDTO> meetingInputs) {
        this.rooms = rooms;
        this.movableTools = movableTools;
        this.meetingInputs = meetingInputs;
    }

    public void setRooms(List<RoomDTO> rooms) {
        this.rooms = rooms;
    }

    public List<MeetingDTO> getMeetingInputs() {
        return meetingInputs;
    }

    public List<ToolType> findRequiredTools(MeetingType meetingType) {
        return switch (meetingType) {
            case VC -> List.of(ToolType.SCREEN, ToolType.CONFERENCE_PHONE, ToolType.WEBCAM);
            case SPEC -> List.of(ToolType.WHITEBOARD);
            case RS -> Collections.emptyList();
            case RC -> List.of(ToolType.WHITEBOARD, ToolType.SCREEN, ToolType.CONFERENCE_PHONE);
        };
    }

    public RoomDTO findRoomByTools(MeetingDTO meetingVC) {
        return rooms.stream()
                .filter(room -> room.getTools().stream().map(Tool::getType).toList()
                        .containsAll(findRequiredTools(meetingVC.getType())))
                .findFirst().orElseThrow();
    }

    public List<RoomDTO> findByCapacity(MeetingDTO meetingDTO) {
        return findByCapacity(meetingDTO.getAttendees());
    }

    public RoomDTO findRoom(MeetingDTO meetingDTO) {
        return findRoomByTools(meetingDTO);
    }
}
