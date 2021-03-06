@ECHO off
COLOR 3

REM SETX /M JAVA_HOME "C:\Program Files\Java\jdk1.7.0_17" > nul
SETX /M JAVA_HOME "C:\Program Files\Java\jdk1.8.0" > nul
SETX /M JAVA_TOOL_OPTIONS -Dfile.encoding=UTF8 > nul

rem ## Save return path
pushd %~dp0

set ANT=%~dp0Tools\Ant\bin\ant

echo.
echo =============================
echo Building new Parser
echo =============================
echo.

CD "%~dp0Sources"

CALL "%ANT%" clean dist

SET Parser="%~dp0Sources\build\Parser.zip"
IF NOT EXIST %Parser% GOTO BUILD_PATH_ERROR

echo.
echo =============================
echo Cleaning old Parser's files
echo =============================
echo.

CD "%~dp0Builds"

DEL /S /F /Q Parser > nul && echo Done !

echo.
echo =============================
echo Unzipping new Parser's files
echo =============================
echo.

cd "%~dp0Builds"

CALL "%~dp0Tools\unzip.exe" -q %Parser% && echo Done !

echo.
echo ===============================
echo Cleaning temporary build files
echo ===============================
echo.

CD "%~dp0Sources"

CALL "%ANT%" clean > nul && echo Done !

GOTO QUIT

:BUILD_PATH_ERROR
echo.
echo ==============================================
echo Build Parser.zip could not be found
echo Please check the name/location of build files.
echo ==============================================
echo.
pause.

:QUIT
echo.
echo ======================================================
echo Every operation was done successfully !
echo You may now use your parser.
echo ======================================================
echo.
pause.