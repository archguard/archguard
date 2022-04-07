#!/bin/bash

version=${1}

echo "current version: ${version}"

curl -sSl https://raw.githubusercontent.com/archguard/archguard/${version}/docker-compose.yml > docker-compose.yaml

if [ ! -f "./docker-compose.yaml" ];then
    # shellcheck disable=SC2016
    exit 0
fi

sudo docker-compose up -d