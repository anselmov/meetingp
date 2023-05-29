package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.entity.ToolEntity;
import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.Tool;
import com.ndekefa.meetingp.model.ToolType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class ToolServiceTest {

    private ToolService toolService;

    @BeforeEach
    public void setUp() {
        toolService = new ToolService();

    }

    @Test
    public void should_find_required_tools_by_meeting_type_rs() {
        MeetingDTO meetingVC = MeetingDTO.builder().type(MeetingType.RS).build();
        assertThat(toolService.findRequiredTools(meetingVC.getType())).isEmpty();
    }

    @Test
    public void should_check_required_tools_rs() {
        List<ToolEntity> roomTools = Collections.emptyList();
        assertThat(toolService.hasRequiredTools(roomTools, MeetingType.RS)).isTrue();

        roomTools = List.of(
                new ToolEntity(ToolType.SCREEN),
                new ToolEntity(ToolType.CONFERENCE_PHONE),
                new ToolEntity(ToolType.WEBCAM));
        assertThat(toolService.hasRequiredTools(roomTools, MeetingType.RS)).isTrue();
    }

    @Test
    public void should_check_required_tools_rs_null() {
        assertThatThrownBy(() -> toolService.hasRequiredTools(null, MeetingType.RS))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> toolService.hasRequiredTools(Collections.emptyList(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void should_find_required_tools_by_meeting_type_vc() {
        MeetingDTO meetingVC = MeetingDTO.builder().type(MeetingType.VC).build();
        assertThat(meetingVC.getType()).isEqualTo(MeetingType.VC);

        assertThat(toolService.findRequiredTools(meetingVC.getType()))
                .containsAll(List.of(
                        new ToolEntity(ToolType.SCREEN),
                        new ToolEntity(ToolType.CONFERENCE_PHONE),
                        new ToolEntity(ToolType.WEBCAM)));
    }

    @Test
    public void should_check_required_tools_vc_ok() {
        List<ToolEntity> initialTools = List.of(new ToolEntity(ToolType.SCREEN), new ToolEntity(ToolType.WEBCAM), new ToolEntity((ToolType.CONFERENCE_PHONE)));
        boolean hasRequiredTools = toolService.hasRequiredTools(initialTools, MeetingType.VC);
        assertThat(hasRequiredTools).isTrue();
    }

    @Test
    public void should_check_required_tools_vc_ko() {
        List<ToolEntity> initialTools = List.of(new ToolEntity(ToolType.SCREEN));
        boolean hasRequiredTools = toolService.hasRequiredTools(initialTools, MeetingType.VC);
        assertThat(hasRequiredTools).isFalse();
    }

    @Test
    public void should_find_required_tools_completing_missing_tools_ok() {
        List<ToolEntity> initialTools = List.of(new ToolEntity(ToolType.SCREEN));
        List<ToolEntity> expectedTools = List.of(
                new ToolEntity(ToolType.SCREEN),
                new ToolEntity(ToolType.WEBCAM),
                new ToolEntity(ToolType.CONFERENCE_PHONE));
        List<ToolEntity> finalTools = toolService.findRequiredTools(initialTools, MeetingType.VC);
        assertThat(finalTools).containsAll(expectedTools);
    }

    @Test
    public void should_find_required_tools_completing_missing_tools_ko() {
        List<ToolEntity> initialTools = List.of(new ToolEntity(ToolType.SCREEN));
        toolService.setMovableTools(Collections.emptyList());
        List<ToolEntity> finalTools = toolService.findRequiredTools(initialTools, MeetingType.VC);
        assertThat(finalTools).doesNotContain(new ToolEntity(ToolType.WEBCAM), new ToolEntity(ToolType.CONFERENCE_PHONE));
    }
}