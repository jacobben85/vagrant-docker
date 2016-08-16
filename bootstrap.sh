#!/usr/bin/env bash

echo "Installing htop"
apt-get install -y htop

echo "Installing java"
add-apt-repository ppa:webupd8team/java -y
apt-get update -y
apt-get install oracle-java8-installer -y

echo "Installing elasticsearch"
wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-2.3.0.deb
dpkg -i elasticsearch-2.3.0.deb
update-rc.d elasticsearch defaults 95 10
/etc/init.d/elasticsearch start
