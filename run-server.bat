@echo off
setlocal enabledelayedexpansion

set DIR=%~dp0
if exist "%DIR%server\target\actest-server.jar" (
    set JAR=%DIR%server\target\actest-server.jar
) else if exist "%DIR%actest-server.jar" (
    set JAR=%DIR%actest-server.jar
) else (
    echo [ERROR] actest-server.jar not found. Please run 'mvn clean package' first.
    pause
    exit /b 1
)

echo Starting ACTEST Server with %JAR%...
java -jar "%JAR%" %*
