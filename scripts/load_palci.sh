#!/bin/bash -e

QTEST=`echo '{  "value":"one" }' | jq -r ".value"`

if [ $JQREST="one" ]
then
  echo JQ installed and working
else
  echo Please install JQ
  exit 1
fi

echo "Load JSON file"

# Because of the template placeholders in the file now, we load passing in empty object as substitutions.
json_data_file=`cat palci.jq`
json_data=`echo '{}' | jq "$json_data_file"`
json_result="$json_data"
IFS=$'\n'       # make newlines the only separator

count=0
for row in $(echo "$json_result" | jq -rc ".entries[]" ); do
  echo "Posting ${row}"
  # result=$(curl -sSL -H 'Accept:application/json' -H 'Content-Type: application/json' -H 'X-OKAPI-TENANT: diku' -XPOST "$TARGET/licenses/custprops" -d "${row}")
  # echo $result | jq
  # json_data=`echo "$json_data" | jq ".propertyDefinitions[$count] = $result"`
  count=$((count+1))
done

