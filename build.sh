#!/bin/sh

mvn assembly:assembly -DdescriptorId=jar-with-dependencies

echo "file is ready in ./target/StreamMeDownloader-1.0-SNAPSHOT-jar-with-dependencies.jar"


echo "run 'java -jar target/StreamMeDownloader-1.0-SNAPSHOT-jar-with-dependencies.jar' to launch"
