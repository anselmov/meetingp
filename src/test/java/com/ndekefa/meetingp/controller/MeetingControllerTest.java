package com.ndekefa.meetingp.controller;

import com.ndekefa.meetingp.data.dto.MeetingDTO;
import com.ndekefa.meetingp.data.dto.MeetingDTOTest;
import com.ndekefa.meetingp.model.MeetingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static java.time.LocalDateTime.of;

import java.time.LocalDate;
import java.time.LocalTime;
import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MeetingControllerTest {


    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private HttpHeaders headers;
    private URI uri;
    private String baseUrl;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        baseUrl = "http://localhost:" + port + "/meeting";
        uri = new URI(baseUrl);
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void should_schedule_all_meetings() {
        MeetingDTOTest.buildMeetingInputs().stream().forEach(this::assertScheduled);
    }

    @Test
    @Disabled
    public void should_schedule_single_meeting() {
        MeetingDTO meeting = MeetingDTO.builder().name("Single meeting").type(MeetingType.VC).build();
        HttpEntity<MeetingDTO> request = new HttpEntity<>(meeting, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).contains("E3001");
    }

    @Test
    @Disabled
    public void should_schedule_two_meetings() {
        LocalDate currentDate = LocalDate.of(2023, 5, 1);
        MeetingDTO meeting = MeetingDTO.builder().name("Meeting 1/2")
                .startDate(of(currentDate, LocalTime.of(9, 0)))
                .endDate(of(currentDate, LocalTime.of(10, 0)))
                .type(MeetingType.VC).build();
        HttpEntity<MeetingDTO> request = new HttpEntity<>(meeting, headers);
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).contains("E3001");

        MeetingDTO meeting2 = MeetingDTO.builder().name("Meeting 2/2")
                .startDate(of(currentDate, LocalTime.of(9, 0)))
                .endDate(of(currentDate, LocalTime.of(10, 0)))
                .type(MeetingType.VC).build();
        request = new HttpEntity<>(meeting2, headers);
        result = this.restTemplate.postForEntity(uri, request, String.class);
        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).doesNotContain("E3001");
    }

    private void assertScheduled(MeetingDTO meeting) {
        HttpEntity<MeetingDTO> request = new HttpEntity<>(meeting, headers);
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
        if (meeting.getName().equals("Réunion 8")
                || meeting.getName().equals("Réunion 11")
                || meeting.getName().equals("Réunion 12")
                || meeting.getName().equals("Réunion 14")
                || meeting.getName().equals("Réunion 16")
                || meeting.getName().equals("Réunion 17")
                || meeting.getName().equals("Réunion 19")
                || meeting.getName().equals("Réunion 20")
        ) {
            assertThat(result.getStatusCode().value()).isEqualTo(404);
        } else {
            assertThat(result.getStatusCode().value()).isEqualTo(200);
        }
    }
}