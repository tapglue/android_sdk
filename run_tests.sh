#!/usr/bin/env bash

set -ex

adb kill-server
adb devices
(adb logcat > logcat.out) &
LOGCAT_PID=$!
adb shell am start -n com.tapglue.tapgluetests/.TestActivity
sleep 60
kill -9 ${LOGCAT_PID}
cat logcat.out | grep -i 'Tests finished correctly'
