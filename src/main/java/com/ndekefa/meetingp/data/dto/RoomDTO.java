package com.ndekefa.meetingp.data.dto;

import com.ndekefa.meetingp.model.Tool;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomDTO {

    @Id
    String Id;
    String name;
    int capacity;
    List<Tool> tools;
    boolean isAvailable;
}
