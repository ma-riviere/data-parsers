@ECHO off
COLOR 3

REM SETX /M JAVA_HOME "C:\Program Files\Java\jdk1.7.0_17" > nul
SETX /M JAVA_HOME "C:\Program Files\Java\jdk1.8.0" > nul
SETX /M JAVA_TOOL_OPTIONS -Dfile.encoding=UTF8 > nul

set ANT=D:\Games\Parsers\Tools\Ant\bin\ant

echo.
echo =============================
echo Building new EncDec
echo =============================
echo.

CALL "%ANT%" clean dist

SET EncDec="build\EncDec.zip"
IF NOT EXIST %EncDec% GOTO BUILD_PATH_ERROR

echo.
echo =============================
echo Cleaning old EncDec's files
echo =============================
echo.

REM DEL /S /F /Q EncDec > nul && echo Done !

echo.
echo =============================
echo Unzipping new EncDec's files
echo =============================
echo.

ECHO A | CALL "D:\Games\Parsers\Tools\unzip.exe" -q %EncDec% -d .\bin && echo Done !

echo.
echo ===============================
echo Cleaning temporary build files
echo ===============================
echo.

CALL "%ANT%" clean > nul && echo Done !

GOTO QUIT

:BUILD_PATH_ERROR
echo.
echo ==============================================
echo Build EncDec.zip could not be found
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