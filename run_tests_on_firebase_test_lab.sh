#!/usr/bin/env bash
# Exit if a command fails
set -e
set -o pipefail

echo "Building debug and release APKs..."
./gradlew :app:assembleDebug :app:assembleDebugAndroidTest

echo "Downloading google-cloud-sdk..."
wget --quiet --output-document=/tmp/google-cloud-sdk.tar.gz https://dl.google.com/dl/cloudsdk/channels/rapid/google-cloud-sdk.tar.gz
mkdir -p /opt
tar zxf /tmp/google-cloud-sdk.tar.gz --directory /opt
/opt/google-cloud-sdk/install.sh --quiet
source /opt/google-cloud-sdk/path.bash.inc

echo "Authorizing gcloud and setting config defaults..."
gcloud -q components update
gcloud auth activate-service-account --key-file=gcloud-service-key.json
gcloud --quiet config set project ${GOOGLE_PROJECT_ID}

echo "Running Android Tests with Firebase Test Lab..."
gcloud firebase test android run \
   --type instrumentation \
   --app ./app/build/outputs/apk/debug/app-debug.apk \
   --test ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk