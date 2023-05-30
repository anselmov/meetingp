# Meeting planner

## Oracle Database
``
docker run -d -p 1521:1521 -e APP_USER=tata -e APP_USER_PASSWORD=password -e ORACLE_PASSWORD=password -v oracle-volume:/opt/oracle/oradata gvenzl/oracle-free
``
## Test

- MainControllerTest : schedules all meetings
- MeetingPlannerImplTest : testing algorithm details

or also curl_test.sh

## Next steps
### Improve Algorithm

* the planner first attempts to fit meeting in terms of capacity
* it looks

