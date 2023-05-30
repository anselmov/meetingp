curl -X POST -H "Content-Type: application/json" -d '{
"name": "Meeting Name",
"startDate": "2023-05-20T10:00:00",
"endDate": "2023-05-20T12:00:00",
"type": "VC",
"attendees": 10
}' http://localhost:8080/meeting

curl http://localhost:8080/meeting
