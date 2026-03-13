@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF)
@REM Maven Wrapper startup batch script for Windows
@REM ----------------------------------------------------------------------------

@echo off
setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_PROPERTIES="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties"

@REM Download maven-wrapper.jar if it doesn't exist
if not exist %WRAPPER_JAR% (
    echo Downloading Maven Wrapper...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar'"
)

@REM Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVACMD=java.exe
goto checkMavenHome

:findJavaFromJavaHome
set JAVACMD=%JAVA_HOME%\bin\java.exe

:checkMavenHome
@REM Determine Maven home from wrapper
for /f "tokens=1,2 delims==" %%a in ('type %WRAPPER_PROPERTIES%') do (
    if "%%a"=="distributionUrl" set MAVEN_DIST_URL=%%b
)

@REM Set Maven installation directory
set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6
set MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd

if exist %MAVEN_CMD% goto runMaven

@REM Download and extract Maven
echo Maven not found. Downloading Maven 3.9.6...
set MAVEN_ZIP=%TEMP%\apache-maven-3.9.6-bin.zip
powershell -Command "Invoke-WebRequest -Uri '%MAVEN_DIST_URL%' -OutFile '%MAVEN_ZIP%'"
powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%USERPROFILE%\.m2\wrapper\dists' -Force"
echo Maven downloaded and extracted.

:runMaven
%MAVEN_HOME%\bin\mvn.cmd %*
