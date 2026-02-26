#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Set local scope for the variables with windows NT shell
if [ -n "$CYGWIN" ] || [ -n "$MINGW" ] || [ -n "$MSYS" ]; then
    SEP=";"
else
    SEP=":"
fi

DIRNAME=$(dirname "$0")
APP_BASE_NAME=$(basename "$0")
APP_HOME=$DIRNAME

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

# Find java.exe
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/bin/java" ] ; then
        JAVA_EXE="$JAVA_HOME/bin/java"
    else
        JAVA_EXE="java"
    fi
else
    JAVA_EXE="java"
fi

# Execute Gradle
"$JAVA_EXE" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS "-Dorg.gradle.appname=$APP_BASE_NAME" -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
