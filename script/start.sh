#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)
echo "현재 쉬고 있는 port : ${IDLE_PORT}"


if [ ${IDLE_PORT} -eq 8081 ]
then
        echo "8081 포트 애플리케이션 실행"
        sudo docker run -d --name sloth-real1 --rm -p 8081:8081 -e "SPRING_PROFILES_ACTIVE=real1" dbfgml741/sloth
else
        echo "8082 포트 애플리케이션 실행"
        sudo docker run -d --name sloth-real2 --rm -p 8082:8082 -e "SPRING_PROFILES_ACTIVE=real2" dbfgml741/sloth
fi

sleep 5