#!/bin/bash -x
image_id=`docker image ls | awk '$2=="<none>"{print $3}'`
container_id=`docker container ls | grep -v -i names | awk '$13=="chat-app"{print $1}'`
docker stop $container_id
docker rm $container_id
docker rmi $image_id
docker run -d --name chat-app -v data1:/opt/services/djangoapp/src -p 8000:8000 sanjanakr/my-app:latest
