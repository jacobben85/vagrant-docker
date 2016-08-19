#!/usr/bin/env bash

echo "Prepping shell scripts"

apt-get --assume-yes update -qq
apt-get --assume-yes install jq -qq

echo "Adding Indexes to Elasticsearch"

COUNTER=0
CURLURL="http://127.0.0.1:9200/boeing/part"
COUNT=$(less /vagrant/parts.json | jq '.Parts | length')
while [  $COUNTER -lt $COUNT ]; do
  CURLDATA=$(less /vagrant/parts.json | jq '.Parts['$COUNTER']')
  RESPONSE=$(curl -XPOST "$CURLURL" -sS -H 'Content-Type: application/json' -d "$CURLDATA")
  echo $RESPONSE
  let COUNTER=COUNTER+1
done

echo "Setup is complete, try http://192.168.56.102:8080/ on the browser to access the application."