#!/bin/bash

./gradlew --refresh-dependencies dependencies --update-locks '*:*'
./gradlew :configuration-test:dependencies --refresh-dependencies --update-locks '*:*'
