#!/bin/bash

# 빌드
./gradlew build -x test

# 실행
java -jar /build/libs/Fundy-BE.jar