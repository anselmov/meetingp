package com.ndekefa.meetingp.data.repository;

import com.ndekefa.meetingp.MeetingpApplication;
import com.ndekefa.meetingp.data.entity.RoomEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MeetingpApplication.class)
class RoomRepositoryTest {

    @Autowired
    private RoomRepository repository;

    @Test
    public void testUpdate() {
        RoomEntity room1 = repository.save(RoomEntity.builder().name("Réunion 1").reservations(Collections.emptyList()).build());
        assertEquals(room1.getName(), "Réunion 1");
        room1.setName("changed");

        repository.save(room1);

        RoomEntity foundEntity = repository.findById(Long.valueOf(room1.getId())).get();
        assertNotNull(foundEntity);
        assertEquals(room1.getName(), foundEntity.getName());
    }

}