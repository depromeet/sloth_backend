#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)
echo "idle port : ${IDLE_PORT}"


RUNNING_RESULT=$(sudo docker ps | grep ${IDLE_PORT})

if [ -z ${RUNNING_RESULT} ]
then
    echo "> No applications are currently running"
    $(sudo docker rm -f "sloth-real1")
    $(sudo docker rm -f "sloth-real2")
    echo "docker rm -f sloth-real1~2"
    # sudo docker pull dbfgml741/sloth
    sudo docker image prune -f
    echo "stop.sh complete"
else

  if [ ${IDLE_PORT} -eq 8081 ]
  then
        echo "8081 application stop"
        $(sudo docker rm -f "sloth-real1")
        echo "docker rm -f sloth-real1"
  else
        echo "8082 application stop"
        $(sudo docker rm -f "sloth-real2")
        echo "docker rm -f sloth-real2"
  fi

  # sudo docker pull dbfgml741/sloth
  sudo docker image prune -f

  sleep 5
  echo "stop.sh complete"

fi


#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)
echo "idle port : ${IDLE_PORT}"


RUNNING_RESULT=$(sudo docker ps | grep ${IDLE_PORT})

if [ -z ${RUNNING_RESULT} ]
then
  echo "> No applications are currently running"
  $(sudo docker rm -f "sloth-real1")
  $(sudo docker rm -f "sloth-real2")
  echo "docker rm -f sloth-real1~2"
  sudo docker pull dbfgml741/sloth
  sudo docker image prune -f
  echo "stop.sh complete"
else

  if [ ${IDLE_PORT} -eq 8081 ]
  then
        echo "8081 application stop"
$(sudo docker rm -f "sloth-real1")
echo "docker rm -f sloth-real1"
  else
        echo "8082 application stop"
$(sudo docker rm -f "sloth-real2")
echo "docker rm -f sloth-real2"
  fi

  sudo docker pull dbfgml741/sloth
  sudo docker image prune -f

  sleep 5
  echo "stop.sh complete"
fi
