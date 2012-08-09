#!/bin/bash

echo "refreshing Bin/ ..."
rm -rf Bin
mkdir Bin

echo "copying files..."
cp -r * Bin

echo "cleaning up..."
cd Bin
rmdir Bin

echo "compiling..."
javac -O NearNeural/*.java
javac -O *.java

echo "cleaning up a bit more..."
rm *.java
rm NearNeural/*.java

echo "Done."
