package com.ndekefa.meetingp.data.entity;

import com.ndekefa.meetingp.model.Tool;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Entity(name = "room")
public class RoomEntity {

    @Id
    String Id;
    String name;
    int capacity;
    @OneToMany
    List<ToolEntity> tools;
    boolean isAvailable;
}
