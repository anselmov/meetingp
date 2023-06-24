package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.entity.ReservationEntity;
import com.ndekefa.meetingp.data.entity.RoomEntity;
import com.ndekefa.meetingp.data.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

@Service
public class MeetingPlannerImpl implements MeetingPlanner {

    private final Float capacityLimit;

    Logger logger = LoggerFactory.getLogger(MeetingPlanner.class);
    @Autowired
    private final RoomRepository roomRepository;

    @Autowired
    private final ToolService toolService;

    public MeetingPlannerImpl(RoomRepository roomRepository, ToolService toolService,
                              @Value("${com.ndekefa.meetingp.capacity.limit}")
                              Float capacityLimit) {
        this.roomRepository = roomRepository;
        this.toolService = toolService;
        this.capacityLimit = capacityLimit;
    }

    @Override
    public Optional<RoomEntity> schedule(MeetingDTO meeting) {
        return roomRepository.findAll().stream()
                .filter(byCapacity(meeting.getAttendees()))
                .filter(room -> canSchedule(meeting.getStartDate(), room))
                .map(roomEntity -> toolService.moveMissingTools(roomEntity, meeting.getType()))
                .min(Comparator.comparing(RoomEntity::getCapacity));
    }

    private static boolean canSchedule(LocalDateTime startDate, RoomEntity room) {
        List<ReservationEntity> reservations = room.getReservations();
        if (reservations == null) return true;
        return reservations.stream().allMatch(reservation -> {
            LocalDateTime cleaningTime = reservation.getEndDate().plusHours(1);
            return startDate.isAfter(cleaningTime);
        });
    }

    final Predicate<RoomEntity> byCapacity(int attendeesCount) {
        return room -> (int) (room.getCapacity() * capacityLimit) >= attendeesCount;
    }


}
