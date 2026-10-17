#!/usr/bin/env bash
set -e
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if [ -f "$DIR/admin/target/actest-admin.jar" ]; then
    JAR="$DIR/admin/target/actest-admin.jar"
elif [ -f "$DIR/actest-admin.jar" ]; then
    JAR="$DIR/actest-admin.jar"
else
    echo "actest-admin.jar not found. Please run 'mvn clean package' first."
    exit 1
fi

echo "Starting ACTEST Admin with $JAR..."
exec java -jar "$JAR" "$@"
