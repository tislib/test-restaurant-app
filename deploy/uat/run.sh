#!/bin/bash

java -Dspring.profiles.active=h2,uat -jar /app.jar &

echo 'started'

nginx -g 'daemon off;'

echo 'finish'

