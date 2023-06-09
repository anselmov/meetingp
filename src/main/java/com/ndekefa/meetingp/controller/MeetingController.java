package com.ndekefa.meetingp.controller;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.entity.ReservationEntity;
import com.ndekefa.meetingp.data.entity.RoomEntity;
import com.ndekefa.meetingp.data.repository.RoomRepository;
import com.ndekefa.meetingp.service.MeetingPlanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class MeetingController {

    Logger logger = LoggerFactory.getLogger(MeetingController.class);
    private final RoomRepository repository;
    private final MeetingPlanner planner;

    MeetingController(RoomRepository repository, MeetingPlanner planner) {
        this.repository = repository;
        this.planner = planner;
    }

    @PostMapping(value = "/meeting", consumes = "application/json", produces = "application/json")
    RoomEntity findRoom(@RequestBody MeetingDTO meetingDTO) {
        Optional<RoomEntity> optionalRoom = planner.schedule(meetingDTO);
        if (optionalRoom.isPresent()) {
            RoomEntity room = optionalRoom.get();
            room.addReservation(meetingDTO.getStartDate(), meetingDTO.getEndDate());

            logger.info("Room found for meeting " + meetingDTO.getName() + " = " + room);
            return repository.save(room);
        } else {
            throw new NoSuchElementException("no room found");
        }
    }
}