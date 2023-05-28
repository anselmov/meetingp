package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.dto.MeetingDTOTest;
import com.ndekefa.meetingp.data.dto.RoomDTO;
import com.ndekefa.meetingp.data.dto.RoomDTOTest;
import com.ndekefa.meetingp.model.Tool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

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

    List<RoomDTO> rooms;
    List<Tool> movableTools;
    List<MeetingDTO> meetingInputs;

    @BeforeEach
    void setUp() {
        rooms = RoomDTOTest.buildRooms();
        movableTools = MeetingDTOTest.buildMovableTools();
        meetingInputs = MeetingDTOTest.buildMeetingInputs();
    }

    @Test
    public void should_take_unscheduled_meetings_as_input() {
        assertThat(meetingInputs).noneMatch(MeetingDTO::isScheduled);
    }
    @Test
    public void should_find_the_best_room() {
        // TODO
    }
}