package com.ndekefa.meetingp.model;

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
}
