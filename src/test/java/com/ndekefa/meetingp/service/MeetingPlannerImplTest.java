package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.dto.RoomDTO;
import com.ndekefa.meetingp.data.dto.RoomDTOTest;
import com.ndekefa.meetingp.data.entity.RoomEntity;
import com.ndekefa.meetingp.data.entity.ToolEntity;
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

import java.util.*;
import java.util.stream.Stream;

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
        planner = new MeetingPlannerImpl(roomRepository, toolService);
        toolService.setMovableTools(ToolService.buildMovableTools());
    }

    @Test
    public void should_schedule_by_limited_capacity() {
        // given
        MeetingDTO meeting = MeetingDTO.builder()
                .type(MeetingType.RS).attendees(8).build();
        int attendeesCount = meeting.getAttendees();
        when(roomRepository.findAll()).thenReturn(RoomDTOTest.buildRooms());

        // when
        RoomEntity rooms = planner.findRoom(meeting);

        // then
        Condition<RoomEntity> withLimitedCapacity = new Condition<>(
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
        // the most suitable room has size 12 because int(70% of 12)=8
        RoomEntity suitableRoom = RoomEntity.builder().capacity(12).tools(Collections.emptyList()).build();
        when(roomRepository.findAll()).thenReturn(List.of(
                RoomEntity.builder().tools(Collections.emptyList()).capacity(2).build(),
                RoomEntity.builder().tools(Collections.emptyList()).capacity(20).build(),
                suitableRoom
        ));

        // when
        RoomEntity room = planner.findRoom(meetingWith8Attendees);

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

        RoomEntity room = planner.findRoom(simpleMeeting);

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
        RoomEntity roomByTools = planner.findRoom(meetingVC);

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
            planner.findRoom(meetingWith8attendees);
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
            planner.findRoom(meetingVC);
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
        room = planner.findRoom(meetingVC);

        // then
        assertThat(room).isNotNull();
        assertThat(room.getTools()).containsAll(List.of(
                new ToolEntity(ToolType.SCREEN, false),
                new ToolEntity(ToolType.CONFERENCE_PHONE, true),
                new ToolEntity(ToolType.WEBCAM, true)
        ));
    }

    @Test
    // TODO REMOVE
    public void streamsTest() {
        List<Integer> mainList = Arrays.asList(1, 2, 3);
        List<Integer> requiredInts = Arrays.asList(2, 5);
        List<Integer> movableInts = Arrays.asList(2, 3, 5);

        List<Integer> missing = requiredInts.stream()
                .filter(integer -> !mainList.contains(integer))
                .peek(f -> System.out.println("missing = " + f))
                .toList();

        Optional<Integer> findFirst = missing.stream().filter(movableInts::contains).findFirst();
        if (findFirst.isPresent()) {
            System.out.println("First missing number: " + findFirst.get());
        } else {
            System.out.println("No number found.");
        }
        List<Integer> finalList = Stream.concat(mainList.stream(), missing.stream()).toList();
        System.out.println("Final List: " + finalList); // 1 2 3 5
    }
}