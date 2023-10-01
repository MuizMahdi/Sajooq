#!/bin/bash
./mvnw clean install
sudo docker build . -t sajooq-cli