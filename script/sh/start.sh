#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)
echo "idle port : ${IDLE_PORT}"


if [ ${IDLE_PORT} -eq 8081 ]
then
        echo "Run 8081 port application"
        sudo docker run -d --name sloth-real1 --rm -p 8081:8081 -e "SPRING_PROFILES_ACTIVE=real1" dbfgml741/sloth:latest
else
        echo "Run 8082 port application"
        sudo docker run -d --name sloth-real2 --rm -p 8082:8082 -e "SPRING_PROFILES_ACTIVE=real2" dbfgml741/sloth:latest
fi

sleep 5