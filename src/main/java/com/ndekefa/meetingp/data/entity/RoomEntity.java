package com.ndekefa.meetingp.data.entity;

import com.ndekefa.meetingp.model.Reservation;
import com.ndekefa.meetingp.model.Tool;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
    @GeneratedValue
    private String Id;
    private String name;
    private int capacity;
    @OneToMany
    private List<ToolEntity> tools;
    @OneToMany
    private List<ReservationEntity> reservations;
    private boolean isAvailable;
}
