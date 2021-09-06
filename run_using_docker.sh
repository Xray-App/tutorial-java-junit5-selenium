#!/bin/bash

# please create a settings.xml based on settings.xml.sample, having your GH username and personal token, to be able to access GH maven packages

docker build . -t tutorial_java_junit5_selenium

docker run --rm -v $(pwd)/reports:/source/reports -t tutorial_java_junit5_selenium
