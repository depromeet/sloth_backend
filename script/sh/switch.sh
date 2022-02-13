#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

IDLE_PORT=$1

function switch_proxy() {

    echo "> Port to switch : $IDLE_PORT"
    echo "> Port switching"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

    echo "> Nginx Reload"
    sudo service nginx reload
}