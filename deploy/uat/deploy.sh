#!/bin/bash

cd "$(dirname "$0")" || true

docker build ../.. -f Dockerfile -t restaurant-app
docker kill restaurant-app
docker rm restaurant-app
docker run -d --name restaurant-app -p 8978:80 restaurant-app
