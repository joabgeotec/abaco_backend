#!/bin/bash

echo "Subindo os containers do docker!"

sudo docker-compose -f ./src/main/docker/postgresql.yml up -d
sudo docker-compose -f ./src/main/docker/jhipster-registry.yml up -d
sudo docker-compose -f ./src/main/docker/elasticsearch.yml up -d

echo "Ready!"
