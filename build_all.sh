#!/bin/bash

# Syntropy Delta Labs - Automation Script
# Clean and compile Android project

echo "Cleaning project..."
./gradlew clean

echo "Building applet..."
./gradlew :app:assembleDebug

if [ $? -eq 0 ]; then
  echo "Build successful!"
else
  echo "Build failed!"
  exit 1
fi
