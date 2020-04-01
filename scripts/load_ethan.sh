#!/bin/bash -e

QTEST=`echo '{  "value":"one" }' | jq -r ".value"`
TARGET="http://localhost:9130"
AUTH_TOKEN=`./okapi-login`


if [ $JQREST="one" ]
then
  echo JQ installed and working
else
  echo Please install JQ
  exit 1
fi

echo "Load JSON file"

# Because of the template placeholders in the file now, we load passing in empty object as substitutions.
json_data_file=`cat ethan_test_data.jq`
json_data=`echo '{}' | jq "$json_data_file"`
json_result="$json_data"
IFS=$'\n'       # make newlines the only separator

# Set up the naming authorities first

count=0
for row in $(echo "$json_result" | jq -rc ".entries[]" ); do
  echo "Posting ${row}"
  result=$(curl -sSL -H 'Accept:application/json' -H "X-Okapi-Token: $AUTH_TOKEN" -H 'Content-Type: application/json' -H 'X-OKAPI-TENANT: diku' -XPOST "$TARGET/directory/entry" -d "${row}")
  echo $result
  echo $result | jq
  # json_data=`echo "$json_data" | jq ".propertyDefinitions[$count] = $result"`
  count=$((count+1))
done

