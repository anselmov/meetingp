package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.entity.ReservationEntity;
import com.ndekefa.meetingp.data.entity.RoomEntity;
import com.ndekefa.meetingp.data.entity.ToolEntity;
import com.ndekefa.meetingp.data.repository.RoomRepository;
import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.ToolType;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Scheduling tests using complex criteria.
 * As of 05/2023, the whole algorithm is defined as:
 * <p>
 * a meeting can be scheduled on weekdays between 8am and 8pm
 * for cleaning purposes, a room should be free 1 hour before it starts
 * a room should contain only 70% of its initial capacity
 * VC meetings require a SCREEN, CONFERENCE_PHONE and WEBCAM
 * SPEC meetings require a WHITEBOARD
 * RS meetings require a room capacity over 3
 * RC meetings require a WHITEBOARD, SCREEN and CONFERENCE_PHONE
 * movable tools can also be used
 */
@ExtendWith(MockitoExtension.class)
class MeetingPlannerTest {

    private MeetingPlanner planner;
    @Mock
    private RoomRepository roomRepository;
    private ToolService toolService;

    @BeforeEach
    void setUp() {
        toolService = new ToolService();
        planner = new MeetingPlannerImpl(roomRepository, toolService, 0.7f);
    }

    @Test
    public void should_find_rooms_by_required_tools_and_capacity() {
        // given
        MeetingDTO meetingVC8 = MeetingDTO.builder()
                .name("Réunion 1")
                .type(MeetingType.VC)
                .attendees(8)
                .build();
        RoomEntity mostSuitableRoom = vcRoomWithCapacity(14);
        when(roomRepository.findAll()).thenReturn(List.of(
                vcRoomWithCapacity(20),
                mostSuitableRoom,
                vcRoomWithCapacity(4)));
        // when
        RoomEntity roomByToolsAndCapacity = planner.schedule(meetingVC8).get();

        // then
        assertThat(roomByToolsAndCapacity).isNotNull();
        assertThat(roomByToolsAndCapacity.getTools()).containsExactlyInAnyOrder(
                new ToolEntity(ToolType.SCREEN, false),
                new ToolEntity(ToolType.CONFERENCE_PHONE, false),
                new ToolEntity(ToolType.WEBCAM, false)
        );

        Condition<RoomEntity> withLimitedCapacity = new Condition<>(
                r -> r.getCapacity() * .7 >= meetingVC8.getAttendees(),
                "with room for 70% of initial capacity"
        );
        assertThat(roomByToolsAndCapacity).is(withLimitedCapacity);
        assertThat(roomByToolsAndCapacity).isEqualTo(mostSuitableRoom);
    }

    private static RoomEntity vcRoomWithCapacity(int capacity) {
        return RoomEntity.builder().capacity(capacity).tools(
                List.of(
                        new ToolEntity(ToolType.SCREEN, false),
                        new ToolEntity(ToolType.CONFERENCE_PHONE, false),
                        new ToolEntity(ToolType.WEBCAM, false))).build();
    }

    @Test
    public void should_skip_busy_rooms() {
        LocalDate currentDate = LocalDate.of(2023, 5, 1);
        MeetingDTO meeting1 = MeetingDTO.builder()
                .type(MeetingType.RS)
                .startDate(of(currentDate, LocalTime.of(9, 0)))
                .endDate(of(currentDate, LocalTime.of(10, 0)))
                .attendees(8)
                .build();

        ReservationEntity r1 = new ReservationEntity();
        r1.setStartDate(of(currentDate, LocalTime.of(9, 0)));
        r1.setEndDate(of(currentDate, LocalTime.of(10, 0)));

        when(roomRepository.findAll()).thenReturn(List.of(
                RoomEntity.builder().capacity(20).tools(
                                List.of(
                                        new ToolEntity(ToolType.SCREEN, false),
                                        new ToolEntity(ToolType.CONFERENCE_PHONE, false),
                                        new ToolEntity(ToolType.WEBCAM, false)))
                        .reservations(List.of(r1)).build()));

        assertThatThrownBy(() -> {
            planner.schedule(meeting1).get();
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void test_cant_schedule() {
        LocalDate currentDate = LocalDate.of(2023, 5, 1);
        ReservationEntity r1 = new ReservationEntity();
        r1.setStartDate(of(currentDate, LocalTime.of(9, 0)));
        r1.setEndDate(of(currentDate, LocalTime.of(10, 0)));
        RoomEntity room = RoomEntity.builder().capacity(20).tools(
                        Collections.emptyList())
                .reservations(List.of(r1)).build();
        assertThat(room.getReservations().stream().allMatch(reservation -> {
            LocalDateTime cleaningTime = reservation.getEndDate().plusHours(1);
            return r1.getStartDate().isAfter(cleaningTime);
        })).isFalse();
    }
}