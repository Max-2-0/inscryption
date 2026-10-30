#!/bin/bash

mkdir -p out
javac -cp "deps/*" -d out $(find src -name '*.java')