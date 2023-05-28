package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.dto.MeetingDTOTest;
import com.ndekefa.meetingp.data.dto.RoomDTO;
import com.ndekefa.meetingp.data.dto.RoomDTOTest;
import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.Tool;
import com.ndekefa.meetingp.model.ToolType;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * a meeting can be scheduled on weekdays between 8am and 8pm
 * for cleaning purposes, a room should be free 1 hour before it starts
 * a room should contain only 70% of its initial capacity
 * VC meetings require a SCREEN, CONFERENCE_PHONE and WEBCAM
 * SPEC meetings require a WHITEBOARD
 * RS meetings require a room capacity over 3
 * RC meetings require a WHITEBOARD, SCREEN and CONFERENCE_PHONE
 * movable tools can also be used
 */
class MeetingPlannerTest {

    MeetingPlanner planner = new MeetingPlanner();

    @BeforeEach
    void setUp() {
        List<RoomDTO> rooms = RoomDTOTest.buildRooms();
        List<Tool> movableTools = MeetingDTOTest.buildMovableTools();
        List<MeetingDTO> meetingInputs = MeetingDTOTest.buildMeetingInputs();
        planner.setFixtures(rooms, movableTools, meetingInputs);
    }

    @Test
    public void should_take_unscheduled_meetings_as_input() {
        assertThat(planner.getMeetingInputs()).noneMatch(MeetingDTO::isScheduled);
    }

    // Reunion 2 9h-10h VC 6
    // Reunion 3 11h-12h RC 4
    // Reunion 4 11h-12h RS 2
    @Test
    public void should_schedule_by_limited_capacity() {
        MeetingDTO meeting1 = planner.getMeetingInputs().get(0);
        assertThat(meeting1).isNotNull();

        int attendeesCount = meeting1.getAttendees();
        assertThat(attendeesCount).isEqualTo(8);

        List<RoomDTO> rooms = planner.findByCapacity(attendeesCount);
        assertThat(rooms).hasSize(3);

        Condition<RoomDTO> withLimitedCapacity = new Condition<>(
                r -> r.getCapacity() * .7 >= attendeesCount,
                "with room for 70% of initial capacity"
        );
        assertThat(rooms).are(withLimitedCapacity);
    }

    @Test
    public void should_schedule_by_efficient_capacity() {
        // given
        MeetingDTO meetingWith8Attendees = planner.getMeetingInputs().get(0);
        final int attendeesCount = meetingWith8Attendees.getAttendees();
        RoomDTO suitableRoom = RoomDTO.builder().capacity(12).build(); // int(70% of 12)=8
        planner.setRooms(List.of(
                RoomDTO.builder().capacity(2).build(),
                RoomDTO.builder().capacity(20).build(),
                suitableRoom
        ));

        // when
        RoomDTO room = planner.findByEfficientCapacity(attendeesCount);

        // then
        Condition<RoomDTO> withLimitedCapacity = new Condition<>(
                r -> r.getCapacity() * .7 >= attendeesCount,
                "with room for 70% of initial capacity"
        );
        assertThat(room).is(withLimitedCapacity);
        assertThat(room).isEqualTo(suitableRoom);
    }

    @Test
    public void should_find_required_tools_by_meeting_type() {
        MeetingDTO meetingVC = planner.getMeetingInputs().get(0);
        assertThat(meetingVC.getType()).isEqualTo(MeetingType.VC);

        assertThat(planner.findRequiredTools(meetingVC.getType()))
                .containsAll(List.of(
                        ToolType.SCREEN,
                        ToolType.CONFERENCE_PHONE,
                        ToolType.WEBCAM));
    }

    @Test
    public void should_find_room_by_required_capacity_for_simple_meetings() {
        MeetingDTO simpleMeeting = MeetingDTO.builder().type(MeetingType.RS).build();
        planner.setRooms(List.of(RoomDTO.builder()
                .tools(Collections.emptyList())
                .capacity(4).build()));

        RoomDTO room = planner.findRoom(simpleMeeting);

        assertThat(room).isNotNull();
        assertThat(room.getCapacity()).isGreaterThan(3);
    }

    @Test
    public void should_find_rooms_by_required_tools() {
        // given
        MeetingDTO meetingVC = MeetingDTO.builder().type(MeetingType.VC).build();
        planner.setRooms(List.of(RoomDTO.builder().tools(
                List.of(
                        new Tool(ToolType.SCREEN, false),
                        new Tool(ToolType.CONFERENCE_PHONE, false),
                        new Tool(ToolType.WEBCAM, false))).build()));

        // when
        RoomDTO roomByTools = planner.findRoomByTools(meetingVC);

        // then
        assertThat(roomByTools).isNotNull();
        assertThat(roomByTools.getTools()).hasSize(3);
        assertThat(roomByTools.getTools()).containsExactlyInAnyOrder(
                new Tool(ToolType.SCREEN, false),
                new Tool(ToolType.CONFERENCE_PHONE, false),
                new Tool(ToolType.WEBCAM, false)
        );
    }

    @Test
    public void should_handle_room_not_found_by_capacity() {
        // given
        final int attendeesCount = 8;
        MeetingDTO meetingWith8attendees = MeetingDTO.builder()
                .attendees(attendeesCount).build();
        planner.setRooms(List.of(RoomDTO.builder().capacity(2).build()));

        // when
        List<RoomDTO> rooms = planner.findByCapacity(meetingWith8attendees);

        // then
        assertThat(rooms).hasSize(0);
    }

    @Test
    public void should_handle_room_not_found_by_tool() {
        MeetingDTO meetingVC = MeetingDTO.builder()
                .type(MeetingType.VC).build();
        planner.setRooms(List.of(RoomDTO.builder().tools(Collections.emptyList()).build()));

        assertThatThrownBy(() -> {
            planner.findRoomByTools(meetingVC);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void should_find_rooms_by_required_tools_and_capacity() {
        // given
        MeetingDTO meetingVC8 = planner.getMeetingInputs().get(0);
        planner.setRooms(List.of(RoomDTO.builder().capacity(12).tools(
                List.of(
                        new Tool(ToolType.SCREEN, false),
                        new Tool(ToolType.CONFERENCE_PHONE, false),
                        new Tool(ToolType.WEBCAM, false))).build()));
        // when
        RoomDTO roomByToolsAndCapacity = planner.findRoom(meetingVC8);

        // then
        assertThat(roomByToolsAndCapacity).isNotNull();
        assertThat(roomByToolsAndCapacity.getTools()).containsExactlyInAnyOrder(
                new Tool(ToolType.SCREEN, false),
                new Tool(ToolType.CONFERENCE_PHONE, false),
                new Tool(ToolType.WEBCAM, false)
        );

        Condition<RoomDTO> withLimitedCapacity = new Condition<>(
                r -> r.getCapacity() * .7 >= meetingVC8.getAttendees(),
                "with room for 70% of initial capacity"
        );
        assertThat(roomByToolsAndCapacity).is(withLimitedCapacity);
    }

    @Test
    public void should_find_the_best_room() {
        // TODO
    }
}