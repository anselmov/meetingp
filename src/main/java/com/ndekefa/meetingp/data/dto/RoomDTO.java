package com.ndekefa.meetingp.data.dto;

import com.ndekefa.meetingp.model.Tool;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomDTO {

    String name;
    int capacity;
    List<Tool> tools;
    boolean isAvailable;
}
