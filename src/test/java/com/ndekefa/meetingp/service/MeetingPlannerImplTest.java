package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.dto.RoomDTO;
import com.ndekefa.meetingp.data.dto.RoomDTOTest;
import com.ndekefa.meetingp.data.repository.RoomRepository;
import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.Tool;
import com.ndekefa.meetingp.model.ToolType;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingPlannerImplTest {

    MeetingPlannerImpl planner;
    @Mock
    RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        planner = new MeetingPlannerImpl(roomRepository);
    }

    @Test
    public void should_schedule_by_limited_capacity() {
        // given
        MeetingDTO meeting = MeetingDTO.builder()
                .type(MeetingType.RS).attendees(8).build();
        int attendeesCount = meeting.getAttendees();
        when(roomRepository.findAll()).thenReturn(RoomDTOTest.buildRooms());

        // when
        RoomDTO rooms = planner.findRoom(meeting);

        // then
        Condition<RoomDTO> withLimitedCapacity = new Condition<>(
                r -> r.getCapacity() * .7 >= attendeesCount,
                "with room for 70% of initial capacity"
        );
        assertThat(rooms).is(withLimitedCapacity);
    }

    @Test
    public void should_schedule_by_efficient_capacity() {
        // given
        MeetingDTO meetingWith8Attendees = MeetingDTO.builder().type(MeetingType.RS).attendees(8).build();
        final int attendeesCount = meetingWith8Attendees.getAttendees();
        RoomDTO suitableRoom = RoomDTO.builder().capacity(12).build(); // int(70% of 12)=8
        when(roomRepository.findAll()).thenReturn(List.of(
                RoomDTO.builder().tools(Collections.emptyList()).capacity(2).build(),
                RoomDTO.builder().tools(Collections.emptyList()).capacity(20).build(),
                suitableRoom
        ));

        // when
        RoomDTO room = planner.findRoom(meetingWith8Attendees);

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
        MeetingDTO meetingVC = MeetingDTO.builder().type(MeetingType.VC).build();
        assertThat(meetingVC.getType()).isEqualTo(MeetingType.VC);

        assertThat(planner.findRequiredTools(meetingVC.getType()))
                .containsAll(List.of(
                        new Tool(ToolType.SCREEN),
                        new Tool(ToolType.CONFERENCE_PHONE),
                        new Tool(ToolType.WEBCAM)));
    }

    @Test
    public void should_find_room_by_required_capacity_for_simple_meetings() {
        MeetingDTO simpleMeeting = MeetingDTO.builder().type(MeetingType.RS).build();
        when(roomRepository.findAll()).thenReturn(
                List.of(RoomDTO.builder()
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
        when(roomRepository.findAll()).thenReturn(
                List.of(RoomDTO.builder()
                        .tools(
                                List.of(
                                        new Tool(ToolType.SCREEN, false),
                                        new Tool(ToolType.CONFERENCE_PHONE, false),
                                        new Tool(ToolType.WEBCAM, false)
                                )
                        ).build()));

        // when
        RoomDTO roomByTools = planner.findRoom(meetingVC);

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
        MeetingDTO meetingWith8attendees = MeetingDTO.builder()
                .attendees(8).type(MeetingType.VC).build();
        when(roomRepository.findAll()).thenReturn(
                List.of(RoomDTO.builder().capacity(2).build()));

        assertThatThrownBy(() -> {
            planner.findRoom(meetingWith8attendees);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void should_handle_room_not_found_by_tool() {
        MeetingDTO meetingVC = MeetingDTO.builder()
                .type(MeetingType.VC).build();
        when(roomRepository.findAll()).thenReturn(
                List.of(RoomDTO.builder().tools(Collections.emptyList()).build()));

        assertThatThrownBy(() ->
        {
            List<Tool> requiredTools = planner.findRequiredTools(meetingVC.getType());
            assertThat(requiredTools).hasSize(3);
            planner.findRoom(meetingVC);
        })
                .isInstanceOf(NoSuchElementException.class);
    }
}