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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingPlannerImplTest {

    MeetingPlannerImpl planner;
    @Mock
    RoomRepository roomRepository;

    ToolService toolService;

    @BeforeEach
    void setUp() {
        toolService = new ToolService();
        planner = new MeetingPlannerImpl(roomRepository, toolService, 0.7f);
        toolService.setMovableTools(ToolService.buildMovableTools());
    }

    @Test
    public void should_schedule_by_efficient_capacity() {
        // given
        MeetingDTO meetingWith8Attendees = MeetingDTO.builder().type(MeetingType.RS).attendees(8).build();
        final int attendeesCount = meetingWith8Attendees.getAttendees();
        // the most suitable room has size 12 because int(70% of 12)=8
        RoomEntity suitableRoom = RoomEntity.builder().capacity(12).tools(Collections.emptyList()).build();
        when(roomRepository.findAll()).thenReturn(List.of(
                RoomEntity.builder().tools(Collections.emptyList()).capacity(2).build(),
                RoomEntity.builder().tools(Collections.emptyList()).capacity(20).build(),
                suitableRoom
        ));

        // when
        RoomEntity room = planner.schedule(meetingWith8Attendees).get();

        // then
        Condition<RoomEntity> withLimitedCapacity = new Condition<>(
                r -> r.getCapacity() * .7 >= attendeesCount,
                "with room for 70% of initial capacity"
        );
        assertThat(room).is(withLimitedCapacity);
        assertThat(room).isEqualTo(suitableRoom);
    }


    @Test
    public void should_find_room_by_required_capacity_for_simple_meetings() {
        MeetingDTO simpleMeeting = MeetingDTO.builder().type(MeetingType.RS).build();
        when(roomRepository.findAll()).thenReturn(
                List.of(RoomEntity.builder()
                        .tools(Collections.emptyList())
                        .capacity(4).build()));

        RoomEntity room = planner.schedule(simpleMeeting).get();

        assertThat(room).isNotNull();
        assertThat(room.getCapacity()).isGreaterThan(3);
    }

    @Test
    public void should_find_rooms_by_required_tools() {
        // given
        MeetingDTO meetingVC = MeetingDTO.builder().type(MeetingType.VC).build();
        when(roomRepository.findAll()).thenReturn(
                List.of(RoomEntity.builder()
                        .tools(
                                List.of(
                                        new ToolEntity(ToolType.SCREEN, false),
                                        new ToolEntity(ToolType.CONFERENCE_PHONE, false),
                                        new ToolEntity(ToolType.WEBCAM, false)
                                )
                        ).build()));

        // when
        RoomEntity roomByTools = planner.schedule(meetingVC).get();

        // then
        assertThat(roomByTools).isNotNull();
        assertThat(roomByTools.getTools()).hasSize(3);
        assertThat(roomByTools.getTools()).containsExactlyInAnyOrder(
                new ToolEntity(ToolType.SCREEN, false),
                new ToolEntity(ToolType.CONFERENCE_PHONE, false),
                new ToolEntity(ToolType.WEBCAM, false)
        );
    }


    @Test
    public void should_handle_room_not_found_by_capacity() {
        MeetingDTO meetingWith8attendees = MeetingDTO.builder()
                .attendees(8).type(MeetingType.VC).build();
        when(roomRepository.findAll()).thenReturn(
                List.of(RoomEntity.builder().capacity(2).build()));

        assertThatThrownBy(() -> {
            planner.schedule(meetingWith8attendees).get();
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void should_handle_room_not_found_by_tool() {
        MeetingDTO meetingVC = MeetingDTO.builder()
                .type(MeetingType.VC).attendees(6).build();
        when(roomRepository.findAll()).thenReturn(
                List.of(RoomEntity.builder().tools(Collections.emptyList()).build()));
        toolService.setMovableTools(Collections.emptyList());

        assertThatThrownBy(() ->
        {
            planner.schedule(meetingVC).get();
        })
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void should_find_rooms_by_required_tools_with_movable_tools() {
        // given
        MeetingDTO meetingVC = MeetingDTO.builder()
                .type(MeetingType.VC).build();
        RoomEntity room = RoomEntity.builder()
                .tools(List.of(new ToolEntity(ToolType.SCREEN))).build();
        when(roomRepository.findAll()).thenReturn(List.of(room));

        // when
        room = planner.schedule(meetingVC).get();

        // then
        assertThat(room).isNotNull();
        assertThat(room.getTools()).containsAll(List.of(
                new ToolEntity(ToolType.SCREEN, false),
                new ToolEntity(ToolType.CONFERENCE_PHONE, true),
                new ToolEntity(ToolType.WEBCAM, true)
        ));
    }
}