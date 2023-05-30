package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.entity.ToolEntity;
import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.ToolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class ToolService {
    Logger logger = LoggerFactory.getLogger(ToolService.class);
    private List<ToolEntity> movableTools = buildMovableTools(); // TODO: refactor to find from repository

    public static List<ToolEntity> buildMovableTools() {
        Map<ToolType, Integer> movableToolQuantities = new HashMap<>();
        movableToolQuantities.put(ToolType.CONFERENCE_PHONE, 4);
        movableToolQuantities.put(ToolType.SCREEN, 5);
        movableToolQuantities.put(ToolType.WEBCAM, 4);
        movableToolQuantities.put(ToolType.WHITEBOARD, 2);

        List<ToolEntity> movableTools = new ArrayList<>();
        for (Map.Entry<ToolType, Integer> entry : movableToolQuantities.entrySet()) {
            ToolType toolType = entry.getKey();
            int quantity = entry.getValue();
            for (int i = 0; i < quantity; i++) {
                movableTools.add(new ToolEntity(toolType, true));
            }
        }
        return movableTools;
    }

    void setMovableTools(List<ToolEntity> movableTools) {
        this.movableTools = movableTools;
    }

    public List<ToolEntity> getMovableTools() {
        return movableTools;
    }


    List<ToolEntity> findRequiredTools(MeetingType meetingType) {
        return switch (meetingType) {
            case VC ->
                    List.of(new ToolEntity(ToolType.SCREEN), new ToolEntity(ToolType.CONFERENCE_PHONE), new ToolEntity(ToolType.WEBCAM));
            case SPEC -> List.of(new ToolEntity(ToolType.WHITEBOARD));
            case RS -> Collections.emptyList();
            case RC ->
                    List.of(new ToolEntity(ToolType.WHITEBOARD), new ToolEntity(ToolType.SCREEN), new ToolEntity(ToolType.CONFERENCE_PHONE));
        };
    }

    /**
     * when required tools for a meeting are missing
     * try to complete roomTools with movable tools
     * return roomTools || roomTools + missing tools
     */
    public List<ToolEntity> findRequiredTools(List<ToolEntity> roomTools, MeetingType meetingType) {
        List<ToolEntity> requiredTools = findRequiredTools(meetingType);
        List<ToolEntity> missing = requiredTools.stream()
                .filter(tool -> !roomTools.contains(tool))
                .peek(tool -> logger.debug("missing tool = " + tool.getType()))
                .toList();
        if (new HashSet<>(movableTools).containsAll(missing)) {
            missing.forEach(this::removeMovableTool);
            return new ArrayList<>(Stream.concat(missing.stream(), roomTools.stream()).toList());
        }
        return roomTools;
    }

    private void removeMovableTool(ToolEntity missing) {
        boolean removed = movableTools.stream()
                .filter(movable -> movable.getType() == missing.getType())
                .findFirst()
                .map(movableTools::remove)
                .orElse(false);
        logger.debug("movable tool " + missing + " removed = " + removed);
    }

    public boolean hasRequiredTools(List<ToolEntity> tools, MeetingType meetingType) {
        List<ToolEntity> requiredTools = findRequiredTools(meetingType);
        requiredTools.forEach(tool -> logger.debug("required Tool = " + tool.getType()));
        return new HashSet<>(tools).containsAll(requiredTools);
    }
}
