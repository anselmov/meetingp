package com.ndekefa.meetingp.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    public void addReservation(LocalDateTime startDate, LocalDateTime endDate) {
        reservations.add(new ReservationEntity(startDate, endDate));
    }
}
