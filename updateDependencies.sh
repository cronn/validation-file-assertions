#!/bin/bash

./gradlew --refresh-dependencies dependencies configuration-test:dependencies --update-locks '*:*'
