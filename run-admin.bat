@echo off
setlocal enabledelayedexpansion

set DIR=%~dp0
if exist "%DIR%admin\target\actest-admin.jar" (
    set JAR=%DIR%admin\target\actest-admin.jar
) else if exist "%DIR%actest-admin.jar" (
    set JAR=%DIR%actest-admin.jar
) else (
    echo [ERROR] actest-admin.jar not found. Please run 'mvn clean package' first.
    pause
    exit /b 1
)

echo Starting ACTEST Admin with %JAR%...
java -jar "%JAR%" %*
