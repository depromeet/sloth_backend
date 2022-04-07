#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)
echo "현재 쉬고 있는 port : ${IDLE_PORT}"


RUNNING_RESULT=$(sudo docker ps | grep ${IDLE_PORT})

if [ -z ${RUNNING_RESULT} ]
then
      echo "> No applications are currently running"
else

  if [ ${IDLE_PORT} -eq 8081 ]
  then
        echo "8081 application stop"
        $(sudo docker stop "spring-template-real1")
  else
        echo "8082 application stop"
        $(sudo docker stop "spring-template-real2")
  fi

  # 도커 이미지 pull
  sudo docker pull dbfgml741/sloth

  #미사용 도커 이미지 삭제
  sudo docker image prune -f

  sleep 5

fi