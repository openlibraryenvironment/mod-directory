BASEDIR=$(dirname "$0")
# echo Please make sure you have run ./gradlew clean generateDescriptors before starting this script

# setOkapiUrl sets the variable OKAPI_URL
. ./setOkapiUrl

pushd "$BASEDIR/../service"

# Check for decriptor target directory.

DESCRIPTORDIR="build/resources/main/okapi"

if [ ! -d "$DESCRIPTORDIR" ]; then
    echo "No descriptors found. Let's try building them."
    ./gradlew generateDescriptors
fi

DEP_DESC=`cat ${DESCRIPTORDIR}/DeploymentDescriptor.json`
SVC_ID=`echo $DEP_DESC | jq -rc '.srvcId'`
INS_ID=`echo $DEP_DESC | jq -rc '.instId'`

echo register and enable $SVC_ID / $INS_ID

echo -e "\n\nREMOVING EXISTING MODULES:"
curl -XDELETE "${OKAPI_URL}/_/proxy/tenants/diku/modules/${SVC_ID}"
curl -XDELETE "${OKAPI_URL}/_/discovery/modules/${SVC_ID}/${INS_ID}"
curl -XDELETE "${OKAPI_URL}/_/proxy/modules/${SVC_ID}"
# ./gradlew clean generateDescriptors
echo -e "\n\nPOSTING MODULE DESCRIPTOR:"
curl -XPOST "${OKAPI_URL}/_/proxy/modules" -d @"${DESCRIPTORDIR}/ModuleDescriptor.json"
echo -e "\n\nPOSTING DEPLOYMENT DESCRIPTOR:"
curl -XPOST "${OKAPI_URL}/_/discovery/modules" -d "$DEP_DESC"

echo -e "\n\nENABLING MODULE:"
echo Deployment Descriptor : $DEP_DESC

ENABLE_DOC=`echo $DEP_DESC | jq -c '[{id: .srvcId, action: "enable"}]'`
echo "Enable service - enable doc is $ENABLE_DOC"

curl -XPOST ${OKAPI_URL}'/_/proxy/tenants/diku/install?tenantParameters=loadSample%3Dtest,loadReference%3Dother' -d "$ENABLE_DOC"

popd
