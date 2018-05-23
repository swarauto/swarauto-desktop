#!/bin/bash

if ! [[ "$(java -version 2>&1)" == *"Java(TM) SE Runtime"* ]]; then
    echo 'Must install JAVA first';
else
    echo "Java already installed $(java -version)";
    DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
    cd $DIR
    java -jar "bin/swarauto-desktop.jar"
fi