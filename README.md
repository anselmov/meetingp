# Meeting planner

A REST API to schedule meetings !

* Supports POST meetings (1 by 1)

## The algorithm

The room search is using the following criteria:
* capacity : 70% of initial capacity limit is defined in application.properties `com.ndekefa.meetingp.capacity.limit=0.7`
* reservations : including previous meeting room & cleaning time
* tools availability : meeting type is translated to tools requirement

```java
public Optional<RoomEntity> schedule(MeetingDTO meeting) {
        return roomRepository.findAll().stream()
        .filter(byCapacity(meeting.getAttendees()))
        .filter(room -> canSchedule(meeting.getStartDate(), room))
        .map(roomEntity -> toolService.moveMissingTools(roomEntity, meeting.getType()))
        .min(Comparator.comparing(RoomEntity::getCapacity));
}
```
## Oracle Database
``
docker run -d -p 1521:1521 -e APP_USER=tata -e APP_USER_PASSWORD=password -e ORACLE_PASSWORD=password -v oracle-volume:/opt/oracle/oradata gvenzl/oracle-free
``
## Test

- MainControllerTest : schedules 20 meetings (oracledb should be running)
- MeetingPlannerImplTest : testing algorithm details
- ToolServiceTest : testing tools related methods

or also curl_test.sh

```
curl -X POST -H "Content-Type: application/json" -d '{
"name": "Meeting Name",
"startDate": "2023-05-20T10:00:00",
"endDate": "2023-05-20T12:00:00",
"type": "VC",
"attendees": 10
}' http://localhost:8080/meeting

```

Meeting types are :
- VC : video conference
- SPEC : sharing knowledge and case studies
- RS : simple in person meeting
- RC : meeting with remote peers
