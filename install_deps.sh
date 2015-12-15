#!/usr/bin/env bash

if [ ! -d ${ANDROID_HOME}/build-tools/23.0.2 ]; then
    echo y | android update sdk --no-ui --all --filter "tool"
    echo y | android update sdk --no-ui --all --filter "build-tools-23.0.2"
fi

echo n | android create avd -n tapglue -f -t android-23