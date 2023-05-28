package com.ndekefa.meetingp.data.dto;

import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.Tool;
import com.ndekefa.meetingp.model.ToolType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MeetingDTOTest {

    @Test
    public void should_build_20_input_meetings() {
        assertThat(buildMeetingInputs()).hasSize(20);
    }

    @Test
    public void should_build_15_movable_tools() {
        assertThat(buildMovableTools()).hasSize(15).allMatch(Tool::isMovable);
    }

    @Test
    public void should_provide_unscheduled_meetings() {
        assertThat(buildMeetingInputs()).noneMatch(MeetingDTO::isScheduled);
    }
    public static List<MeetingDTO> buildMeetingInputs() {
        LocalDate currentDate = LocalDate.now();

        MeetingDTO meeting1 = MeetingDTO.builder()
                .name("Réunion 1")
                .startDate(of(currentDate, LocalTime.of(9, 0)))
                .endDate(of(currentDate, LocalTime.of(10, 0)))
                .type(MeetingType.VC)
                .attendees(8)
                .build();

        MeetingDTO meeting2 = MeetingDTO.builder()
                .name("Réunion 2")
                .startDate(of(currentDate, LocalTime.of(9, 0)))
                .endDate(of(currentDate, LocalTime.of(10, 0)))
                .type(MeetingType.VC)
                .attendees(6)
                .build();

        MeetingDTO meeting3 = MeetingDTO.builder()
                .name("Réunion 3")
                .startDate(of(currentDate, LocalTime.of(11, 0)))
                .endDate(of(currentDate, LocalTime.of(12, 0)))
                .type(MeetingType.RC)
                .attendees(4)
                .build();

        MeetingDTO meeting4 = MeetingDTO.builder()
                .name("Réunion 4")
                .startDate(of(currentDate, LocalTime.of(11, 0)))
                .endDate(of(currentDate, LocalTime.of(12, 0)))
                .type(MeetingType.RS)
                .attendees(2)
                .build();

        MeetingDTO meeting5 = MeetingDTO.builder()
                .name("Réunion 5")
                .startDate(of(currentDate, LocalTime.of(11, 0)))
                .endDate(of(currentDate, LocalTime.of(12, 0)))
                .type(MeetingType.SPEC)
                .attendees(9)
                .build();

        MeetingDTO meeting6 = MeetingDTO.builder()
                .name("Réunion 6")
                .startDate(of(currentDate, LocalTime.of(9, 0)))
                .endDate(of(currentDate, LocalTime.of(10, 0)))
                .type(MeetingType.RC)
                .attendees(7)
                .build();

        MeetingDTO meeting7 = MeetingDTO.builder()
                .name("Réunion 7")
                .startDate(of(currentDate, LocalTime.of(8, 0)))
                .endDate(of(currentDate, LocalTime.of(9, 0)))
                .type(MeetingType.VC)
                .attendees(9)
                .build();

        MeetingDTO meeting8 = MeetingDTO.builder()
                .name("Réunion 8")
                .startDate(of(currentDate, LocalTime.of(8, 0)))
                .endDate(of(currentDate, LocalTime.of(9, 0)))
                .type(MeetingType.SPEC)
                .attendees(10)
                .build();

        MeetingDTO meeting9 = MeetingDTO.builder()
                .name("Réunion 9")
                .startDate(of(currentDate, LocalTime.of(9, 0)))
                .endDate(of(currentDate, LocalTime.of(10, 0)))
                .type(MeetingType.SPEC)
                .attendees(5)
                .build();

        MeetingDTO meeting10 = MeetingDTO.builder()
                .name("Réunion 10")
                .startDate(of(currentDate, LocalTime.of(9, 0)))
                .endDate(of(currentDate, LocalTime.of(10, 0)))
                .type(MeetingType.RS)
                .attendees(4)
                .build();

        MeetingDTO meeting11 = MeetingDTO.builder()
                .name("Réunion 11")
                .startDate(of(currentDate, LocalTime.of(9, 0)))
                .endDate(of(currentDate, LocalTime.of(10, 0)))
                .type(MeetingType.RC)
                .attendees(8)
                .build();

        MeetingDTO meeting12 = MeetingDTO.builder()
                .name("Réunion 12")
                .startDate(of(currentDate, LocalTime.of(11, 0)))
                .endDate(of(currentDate, LocalTime.of(12, 0)))
                .type(MeetingType.VC)
                .attendees(12)
                .build();

        MeetingDTO meeting13 = MeetingDTO.builder()
                .name("Réunion 13")
                .startDate(of(currentDate, LocalTime.of(11, 0)))
                .endDate(of(currentDate, LocalTime.of(12, 0)))
                .type(MeetingType.SPEC)
                .attendees(5)
                .build();

        MeetingDTO meeting14 = MeetingDTO.builder()
                .name("Réunion 14")
                .startDate(of(currentDate, LocalTime.of(8, 0)))
                .endDate(of(currentDate, LocalTime.of(9, 0)))
                .type(MeetingType.VC)
                .attendees(3)
                .build();

        MeetingDTO meeting15 = MeetingDTO.builder()
                .name("Réunion 15")
                .startDate(of(currentDate, LocalTime.of(8, 0)))
                .endDate(of(currentDate, LocalTime.of(9, 0)))
                .type(MeetingType.SPEC)
                .attendees(2)
                .build();

        MeetingDTO meeting16 = MeetingDTO.builder()
                .name("Réunion 16")
                .startDate(of(currentDate, LocalTime.of(8, 0)))
                .endDate(of(currentDate, LocalTime.of(9, 0)))
                .type(MeetingType.VC)
                .attendees(12)
                .build();

        MeetingDTO meeting17 = MeetingDTO.builder()
                .name("Réunion 17")
                .startDate(of(currentDate, LocalTime.of(10, 0)))
                .endDate(of(currentDate, LocalTime.of(11, 0)))
                .type(MeetingType.VC)
                .attendees(6)
                .build();

        MeetingDTO meeting18 = MeetingDTO.builder()
                .name("Réunion 18")
                .startDate(of(currentDate, LocalTime.of(11, 0)))
                .endDate(of(currentDate, LocalTime.of(12, 0)))
                .type(MeetingType.RS)
                .attendees(2)
                .build();

        MeetingDTO meeting19 = MeetingDTO.builder()
                .name("Réunion 19")
                .startDate(of(currentDate, LocalTime.of(9, 0)))
                .endDate(of(currentDate, LocalTime.of(10, 0)))
                .type(MeetingType.RS)
                .attendees(4)
                .build();

        MeetingDTO meeting20 = MeetingDTO.builder()
                .name("Réunion 20")
                .startDate(of(currentDate, LocalTime.of(9, 0)))
                .endDate(of(currentDate, LocalTime.of(10, 0)))
                .type(MeetingType.RC)
                .attendees(7)
                .build();

        return Arrays.asList(
                meeting1, meeting2, meeting3, meeting4, meeting5,
                meeting6, meeting7, meeting8, meeting9, meeting10,
                meeting11, meeting12, meeting13, meeting14, meeting15,
                meeting16, meeting17, meeting18, meeting19, meeting20
        );

    }

    public static List<Tool> buildMovableTools() {
        Map<ToolType, Integer> movableToolQuantities = new HashMap<>();
        movableToolQuantities.put(ToolType.CONFERENCE_PHONE, 4);
        movableToolQuantities.put(ToolType.SCREEN, 5);
        movableToolQuantities.put(ToolType.WEBCAM, 4);
        movableToolQuantities.put(ToolType.WHITEBOARD, 2);

        List<Tool> movableTools = new ArrayList<>();
        for (Map.Entry<ToolType, Integer> entry : movableToolQuantities.entrySet()) {
            ToolType toolType = entry.getKey();
            int quantity = entry.getValue();
            for (int i = 0; i < quantity; i++) {
                movableTools.add(new Tool(toolType, true));
            }
        }
        return movableTools;
    }
}