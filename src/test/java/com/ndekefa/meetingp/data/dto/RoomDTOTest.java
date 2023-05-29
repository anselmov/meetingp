package com.ndekefa.meetingp.data.dto;

import com.ndekefa.meetingp.data.entity.RoomEntity;
import com.ndekefa.meetingp.data.entity.ToolEntity;
import com.ndekefa.meetingp.model.ToolType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomDTOTest {

    @Test
    public void should_build_12_available_rooms() {
        assertThat(buildRooms()).hasSize(12).allMatch(RoomEntity::isAvailable);
    }

    public static List<RoomEntity> buildRooms() {
        RoomEntity room1 = RoomEntity.builder().name("E1001").capacity(23).isAvailable(true)
                .tools(Collections.emptyList())
                .build();

        RoomEntity room2 = RoomEntity.builder()
                .name("E1002").capacity(10).isAvailable(true)
                .tools(Collections.singletonList(
                        new ToolEntity(ToolType.SCREEN)
                ))
                .build();

        RoomEntity room3 = RoomEntity.builder()
                .name("E1003").capacity(8).isAvailable(true)
                .tools(Collections.singletonList(new ToolEntity(ToolType.CONFERENCE_PHONE)))
                .build();

        RoomEntity room4 = RoomEntity.builder()
                .name("E1004").capacity(4).isAvailable(true)
                .tools(Collections.singletonList(new ToolEntity(ToolType.WHITEBOARD)))
                .build();

        RoomEntity room5 = RoomEntity.builder()
                .name("E2001").capacity(4).isAvailable(true)
                .tools(Collections.emptyList())
                .build();

        RoomEntity room6 = RoomEntity.builder()
                .name("E2002").capacity(15).isAvailable(true)
                .tools(Arrays.asList(
                        new ToolEntity(ToolType.SCREEN),
                        new ToolEntity(ToolType.WEBCAM)
                ))
                .build();

        RoomEntity room7 = RoomEntity.builder()
                .name("E2003").capacity(7).isAvailable(true)
                .tools(Collections.emptyList())
                .build();

        RoomEntity room8 = RoomEntity.builder()
                .name("E2004").capacity(9).isAvailable(true)
                .tools(Collections.singletonList(new ToolEntity(ToolType.WHITEBOARD)))
                .build();

        RoomEntity room9 = RoomEntity.builder()
                .name("E3001").capacity(13).isAvailable(true)
                .tools(Arrays.asList(
                        new ToolEntity(ToolType.SCREEN),
                        new ToolEntity(ToolType.WEBCAM),
                        new ToolEntity(ToolType.CONFERENCE_PHONE)
                ))
                .build();

        RoomEntity room10 = RoomEntity.builder()
                .name("E3002").capacity(8).isAvailable(true)
                .tools(Collections.emptyList())
                .build();

        RoomEntity room11 = RoomEntity.builder()
                .name("E3003").capacity(9).isAvailable(true)
                .tools(Arrays.asList(
                        new ToolEntity(ToolType.SCREEN),
                        new ToolEntity(ToolType.CONFERENCE_PHONE)
                ))
                .build();

        RoomEntity room12 = RoomEntity.builder()
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