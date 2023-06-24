package com.ndekefa.meetingp.data.entity;

import com.ndekefa.meetingp.model.ToolType;
import jakarta.persistence.*;

import java.util.Objects;

import lombok.NoArgsConstructor;

@Entity(name = "tool")
@NoArgsConstructor
public class ToolEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ToolType type;

    private boolean isMovable;

    public ToolEntity(ToolType toolType) {
        this.type = toolType;
        this.isMovable = true;
    }

    public ToolEntity(ToolType toolType, boolean isMovable) {
        this.type = toolType;
        this.isMovable = isMovable;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public ToolType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolEntity tool = (ToolEntity) o;
        return type == tool.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "Tool{" +
                "type=" + type +
                ", isMovable=" + isMovable +
                '}';
    }
}
