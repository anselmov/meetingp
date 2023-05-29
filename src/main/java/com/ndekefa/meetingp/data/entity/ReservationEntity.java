package com.ndekefa.meetingp.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;


@Entity(name = "reservation")
public class ReservationEntity {

    @Id
    @GeneratedValue
    private String Id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

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
