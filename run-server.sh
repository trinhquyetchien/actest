#!/usr/bin/env bash
set -e
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if [ -f "$DIR/server/target/actest-server.jar" ]; then
    JAR="$DIR/server/target/actest-server.jar"
elif [ -f "$DIR/actest-server.jar" ]; then
    JAR="$DIR/actest-server.jar"
else
    echo "actest-server.jar not found. Please run 'mvn clean package' first."
    exit 1
fi

echo "Starting ACTEST Server with $JAR..."
exec java -jar "$JAR" "$@"
