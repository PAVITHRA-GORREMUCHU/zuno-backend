#!/bin/sh
# Maven Wrapper script
# Downloads Maven if not present and runs it

set -e

BASE_DIR=$(cd "$(dirname "$0")" && pwd)
MVN_PROPS="$BASE_DIR/.mvn/wrapper/maven-wrapper.properties"
WRAPPER_JAR="$BASE_DIR/.mvn/wrapper/maven-wrapper.jar"

# Read distribution URL
DIST_URL=$(grep distributionUrl "$MVN_PROPS" | cut -d'=' -f2-)
WRAPPER_URL=$(grep wrapperUrl "$MVN_PROPS" | cut -d'=' -f2-)

# Download wrapper jar if missing
if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Downloading Maven Wrapper..."
  if command -v curl > /dev/null; then
    curl -sL "$WRAPPER_URL" -o "$WRAPPER_JAR"
  elif command -v wget > /dev/null; then
    wget -q "$WRAPPER_URL" -O "$WRAPPER_JAR"
  fi
fi

# Find Java
if [ -n "$JAVA_HOME" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD="java"
fi

exec "$JAVACMD" \
  -classpath "$WRAPPER_JAR" \
  "-Dmaven.multiModuleProjectDirectory=$BASE_DIR" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
