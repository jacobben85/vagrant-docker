#!/usr/bin/env bash

echo "Adding Indexes to Elasticsearch"

COUNTER=0
CURLURL="http://localhost:9200/test/part"
COUNT=$(less /vagrant/scripts/data/parts.json | jq '.Parts | length')
while [  $COUNTER -lt $COUNT ]; do
  CURLDATA=$(less /vagrant/scripts/data/parts.json | jq '.Parts['$COUNTER']')
  RESPONSE=$(curl -XPOST "$CURLURL" -sS -H 'Content-Type: application/json' -d "$CURLDATA")
  echo $RESPONSE
  let COUNTER=COUNTER+1
done

COUNTER2=0
CURLURL2="http://localhost:9200/sample/part"
COUNT2=$(less /vagrant/scripts/data/sample.json | jq '.Sample | length')
while [  $COUNTER2 -lt $COUNT2 ]; do
  CURLDATA2=$(less /vagrant/scripts/data/sample.json | jq '.Sample['$COUNTER2']')
  RESPONSE2=$(curl -XPOST "$CURLURL2" -sS -H 'Content-Type: application/json' -d "$CURLDATA2")
  echo $RESPONSE2
  let COUNTER2=COUNTER2+1
done

echo "Setup is complete, try http://192.168.56.102:8080/ on the browser to access the application."
