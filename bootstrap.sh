#!/usr/bin/env bash

#echo "Installing htop"
#apt-get install -y htop
#
#echo "Installing java"
#add-apt-repository ppa:webupd8team/java -y
#apt-get update -y
#apt-get install oracle-java8-installer -y
#
#echo "Installing elasticsearch"
#wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-2.3.0.deb
#dpkg -i elasticsearch-2.3.0.deb
#update-rc.d elasticsearch defaults 95 10
#/etc/init.d/elasticsearch start

sleep 5

echo "Adding 2 indexes to Elasticsearch"

curl -H "Content-Type: application/json" -X POST http://localhost:9200/boeing/part/1 -d '{"id": "1", "name": "part one", "description": "part description in detail" }'
curl -H "Content-Type: application/json" -X POST http://localhost:9200/boeing/part/2 -d '{"id": "2", "name": "part two", "description": "part description in detail" }'

echo "Setup is complete, try http://192.168.56.102:8080/ on the browser to access the application."