package com.ndekefa.meetingp.data.entity;

import com.ndekefa.meetingp.model.Reservation;
import com.ndekefa.meetingp.model.Tool;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "room")
public class RoomEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private int capacity;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<ToolEntity> tools;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<ReservationEntity> reservations;
    @Builder.Default
    private boolean isAvailable = true;
}
