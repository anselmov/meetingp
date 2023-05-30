package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.entity.ReservationEntity;
import com.ndekefa.meetingp.data.entity.RoomEntity;
import com.ndekefa.meetingp.data.repository.RoomRepository;
import com.ndekefa.meetingp.model.MeetingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    // TODO: clear from duplications
    public RoomEntity findRoom(MeetingDTO meetingDTO) {
        int attendeesCount = meetingDTO.getAttendees();
        MeetingType meetingType = meetingDTO.getType();
        LocalDateTime startDate = meetingDTO.getStartDate();
        List<RoomEntity> allRooms = roomRepository.findAll();

        Optional<RoomEntity> min = allRooms.stream()
                .filter(byCapacity(attendeesCount))
                .filter(RoomEntity::isAvailable)
                .filter(room -> canSchedule(startDate, room))
                .filter(roomDTO -> toolService.hasRequiredTools(roomDTO.getTools(), meetingType))
                .min(Comparator.comparingInt(RoomEntity::getCapacity));

        if (min.isPresent()) {
            logger.info("Available room with required tools found");
            return min.get();

        } else {
            logger.info("Scheduling best room with tools");
            Optional<RoomEntity> optionalRoom = allRooms.stream()
                    .filter(byCapacity(attendeesCount))
                    .filter(RoomEntity::isAvailable)
                    .filter(room -> canSchedule(startDate, room))
                    .min(Comparator.comparingInt(RoomEntity::getCapacity));
            if (optionalRoom.isPresent()) {
                RoomEntity roomDTO1 = optionalRoom.get();
                roomDTO1.setTools(toolService.findRequiredTools(roomDTO1.getTools(), meetingType));
                return roomDTO1;
            }
            NoSuchElementException exception = new NoSuchElementException("No room found for meeting " + meetingDTO);
            logger.info(exception.getMessage(), exception);
            throw exception;
        }
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
