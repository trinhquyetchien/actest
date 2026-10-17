#!/usr/bin/env bash
set -e
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if [ -f "$DIR/client/target/actest-client.jar" ]; then
    JAR="$DIR/client/target/actest-client.jar"
elif [ -f "$DIR/actest-client.jar" ]; then
    JAR="$DIR/actest-client.jar"
else
    echo "actest-client.jar not found. Please run 'mvn clean package' first."
    exit 1
fi

echo "Starting ACTEST Client with $JAR..."
exec java -jar "$JAR" "$@"
