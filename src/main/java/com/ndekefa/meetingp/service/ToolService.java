package com.ndekefa.meetingp.service;

import com.ndekefa.meetingp.data.entity.RoomEntity;
import com.ndekefa.meetingp.data.entity.ToolEntity;
import com.ndekefa.meetingp.model.MeetingType;
import com.ndekefa.meetingp.model.ToolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
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

    /**
     * when required tools for a meeting are missing
     * try to complete roomTools with movable tools
     * missing tools are moved from movableTools
     * return roomTools || roomTools + missing tools
     */
    public RoomEntity moveMissingTools(RoomEntity roomEntity, MeetingType meetingType) {
        roomEntity.setTools(withMissingTools(roomEntity, meetingType));
        removeToolsFromMovableTools(roomEntity);
        return roomEntity;
    }

    private List<ToolEntity> withMissingTools(RoomEntity roomEntity, MeetingType meetingType) {
        Stream<ToolEntity> missingTools = findMissing(roomEntity, findRequiredTools(meetingType)).stream();
        Stream<ToolEntity> currentTools = roomEntity.getTools().stream();
        return Stream.concat(missingTools, currentTools).collect(Collectors.toCollection(ArrayList::new));
    }

    List<ToolEntity> findMissing(RoomEntity roomEntity, List<ToolEntity> requiredTools) {
        return requiredTools.stream()
                .filter(tool -> !roomEntity.getTools().contains(tool))
                .peek(tool -> logger.debug("missing tool = " + tool))
                .collect(Collectors.toCollection(ArrayList::new));
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

    private void removeToolsFromMovableTools(RoomEntity roomEntity) {
        roomEntity.getTools().stream().filter(ToolEntity::isMovable).forEach(movableTools::remove);
    }
}
