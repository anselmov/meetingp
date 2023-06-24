package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.entity.RoomEntity;
import com.ndekefa.meetingp.data.entity.ToolEntity;
import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.ToolType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ToolServiceTest {

    private ToolService toolService;

    @BeforeEach
    public void setUp() {
        toolService = new ToolService();

    }

    @Test
    public void should_find_required_tools_by_meeting_type_rs() {
        assertThat(toolService.moveMissingTools(emptyRoom(), MeetingType.RS).getTools()).isEmpty();
    }

    @Test
    public void should_find_required_tools_by_meeting_type_vc() {
        assertThat(toolService.moveMissingTools(emptyRoom(), MeetingType.VC).getTools())
                .containsAll(List.of(
                        new ToolEntity(ToolType.SCREEN),
                        new ToolEntity(ToolType.CONFERENCE_PHONE),
                        new ToolEntity(ToolType.WEBCAM)));
    }

    @Test
    public void should_find_required_tools_by_meeting_type_spec() {
        assertThat(toolService.moveMissingTools(emptyRoom(), MeetingType.SPEC).getTools())
                .containsAll(List.of(
                        new ToolEntity(ToolType.WHITEBOARD)));
    }

    @Test
    public void should_find_required_tools_by_meeting_type_rc() {
        RoomEntity room = toolService.moveMissingTools(emptyRoom(), MeetingType.RC);
        assertThat(room.getTools()).containsAll(List.of(
                new ToolEntity(ToolType.WHITEBOARD),
                new ToolEntity(ToolType.SCREEN),
                new ToolEntity(ToolType.CONFERENCE_PHONE)
        ));
    }

    @Test
    public void should_remove_movable_tools() {
        List<ToolEntity> movableTools = new ArrayList<>();
        movableTools.add(new ToolEntity(ToolType.WHITEBOARD));
        movableTools.add(new ToolEntity(ToolType.SCREEN));
        movableTools.add(new ToolEntity(ToolType.SCREEN));
        movableTools.add(new ToolEntity(ToolType.CONFERENCE_PHONE));
        toolService.setMovableTools(movableTools);
        RoomEntity roomWithWhiteboard = RoomEntity.builder()
                .tools(List.of(new ToolEntity(ToolType.WHITEBOARD, false))).build();

        toolService.moveMissingTools(roomWithWhiteboard, MeetingType.RC); // RC needs WB+SCREEN+C_PHONE

        assertThat(toolService.getMovableTools()).containsExactlyInAnyOrder(
                new ToolEntity(ToolType.WHITEBOARD),
                new ToolEntity(ToolType.SCREEN)
        );
    }

    private static RoomEntity emptyRoom() {
        return RoomEntity.builder().tools(Collections.emptyList()).build();
    }
}