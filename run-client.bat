@echo off
setlocal enabledelayedexpansion

set DIR=%~dp0
if exist "%DIR%client\target\actest-client.jar" (
    set JAR=%DIR%client\target\actest-client.jar
) else if exist "%DIR%actest-client.jar" (
    set JAR=%DIR%actest-client.jar
) else (
    echo [ERROR] actest-client.jar not found. Please run 'mvn clean package' first.
    pause
    exit /b 1
)

echo Starting ACTEST Client with %JAR%...
java -jar "%JAR%" %*
