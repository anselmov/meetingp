package com.ndekefa.meetingp.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;


@Entity(name = "reservation")
public class ReservationEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ReservationEntity() {
    }

    public ReservationEntity(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "ReservationEntity{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
