package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    MeetingPlanner planner;
    @Mock
    RoomRepository roomRepository;
    ToolService toolService;

    @BeforeEach
    void setUp() {
        toolService = new ToolService();
        planner = new MeetingPlannerImpl(roomRepository, toolService);
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
        RoomEntity roomByToolsAndCapacity = planner.findRoom(meetingVC8);

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
    public void should_find_the_best_room() {
        // TODO
    }
}