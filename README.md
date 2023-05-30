# Meeting planner

A REST API to schedule meetings !

Supports POST meetings (1 by 1) :
```
curl -X POST -H "Content-Type: application/json" -d '{
"name": "Meeting Name",
"startDate": "2023-05-20T10:00:00",
"endDate": "2023-05-20T12:00:00",
"type": "VC",
"attendees": 10
}' http://localhost:8080/meeting
```

## Oracle Database
``
docker run -d -p 1521:1521 -e APP_USER=tata -e APP_USER_PASSWORD=password -e ORACLE_PASSWORD=password -v oracle-volume:/opt/oracle/oradata gvenzl/oracle-free
``
## Test

- MainControllerTest : schedules 20 meetings
- MeetingPlannerImplTest : testing algorithm details

or also curl_test.sh

## The algorithm

Overall, the room search is prioritized from most to least important criteria:
* capacity : 70% of initial capacity limit is defined in application.properties `com.ndekefa.meetingp.capacity.limit=0.7`
* tools availability : meeting type is translated to tools requirement
* reservations : including previous meeting room cleaning time
