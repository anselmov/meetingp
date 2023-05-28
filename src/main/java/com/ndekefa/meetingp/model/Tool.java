package com.ndekefa.meetingp.model;

import java.util.Objects;

public class Tool {
    final ToolType type;
    boolean isMovable;

    public Tool(ToolType toolType) {
        this.type = toolType;
    }

    public Tool(ToolType toolType, boolean isMovable) {
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
        Tool tool = (Tool) o;
        return isMovable == tool.isMovable && type == tool.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, isMovable);
    }
}
