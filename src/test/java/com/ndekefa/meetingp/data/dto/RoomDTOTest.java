package com.ndekefa.meetingp.data.dto;

import com.ndekefa.meetingp.model.Tool;
import com.ndekefa.meetingp.model.ToolType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RoomDTOTest {

    @Test
    public void should_build_12_available_rooms() {
        assertThat(buildRooms()).hasSize(12).allMatch(RoomDTO::isAvailable);
    }

    public static List<RoomDTO> buildRooms() {
        RoomDTO room1 = RoomDTO.builder().name("E1001").capacity(23).isAvailable(true)
                .tools(Collections.emptyList())
                .build();
        RoomDTO room2 = RoomDTO.builder()
                .name("E1002").capacity(10).isAvailable(true)
                .tools(Collections.singletonList(
                        new Tool(ToolType.SCREEN)
                ))
                .build();
        RoomDTO room3 = RoomDTO.builder()
                .name("E1003").capacity(8).isAvailable(true)
                .tools(Collections.singletonList(new Tool(ToolType.CONFERENCE_PHONE)))
                .build();

        RoomDTO room4 = RoomDTO.builder()
                .name("E1004").capacity(4).isAvailable(true)
                .tools(Collections.singletonList(new Tool(ToolType.WHITEBOARD)))
                .build();

        RoomDTO room5 = RoomDTO.builder()
                .name("E2001").capacity(4).isAvailable(true)
                .tools(Collections.emptyList())
                .build();

        RoomDTO room6 = RoomDTO.builder()
                .name("E2002").capacity(15).isAvailable(true)
                .tools(Arrays.asList(
                        new Tool(ToolType.SCREEN),
                        new Tool(ToolType.WEBCAM)
                ))
                .build();

        RoomDTO room7 = RoomDTO.builder()
                .name("E2003").capacity(7).isAvailable(true)
                .tools(Collections.emptyList())
                .build();

        RoomDTO room8 = RoomDTO.builder()
                .name("E2004").capacity(9).isAvailable(true)
                .tools(Collections.singletonList(new Tool(ToolType.WHITEBOARD)))
                .build();

        RoomDTO room9 = RoomDTO.builder()
                .name("E3001").capacity(13).isAvailable(true)
                .tools(Arrays.asList(
                        new Tool(ToolType.SCREEN),
                        new Tool(ToolType.WEBCAM),
                        new Tool(ToolType.CONFERENCE_PHONE)
                ))
                .build();

        RoomDTO room10 = RoomDTO.builder()
                .name("E3002").capacity(8).isAvailable(true)
                .tools(Collections.emptyList())
                .build();

        RoomDTO room11 = RoomDTO.builder()
                .name("E3003").capacity(9).isAvailable(true)
                .tools(Arrays.asList(
                        new Tool(ToolType.SCREEN),
                        new Tool(ToolType.CONFERENCE_PHONE)
                ))
                .build();

        RoomDTO room12 = RoomDTO.builder()
                .name("E3004").capacity(4).isAvailable(true)
                .tools(Collections.emptyList())
                .build();

        return Arrays.asList(
                room1, room2, room3, room4,
                room5, room6, room7, room8,
                room9, room10, room11, room12
        );
    }
}