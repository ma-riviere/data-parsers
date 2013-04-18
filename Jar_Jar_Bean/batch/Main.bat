@ECHO OFF

REM ## NAME MASK XMS XMX INPUT [INPUT_FILTER] OUTPUT [OUTPUT_FILTER]
SET NAME=%~1
SET MASK=%2
SET XMS=%3
SET XMX=%4
SET FILTRE=

IF %MASK%==1 CALL :INPUT_ONLY "%~5"
IF %MASK%==2 CALL :OUTPUT_ONLY "%~5"
IF %MASK%==3 CALL :BOTH "%~5" "%~6"
IF %MASK%==4 CALL :MERGE "%~5" "%~6"
IF %MASK%==5 CALL :MERGE "%~5" "%~6"
IF %MASK%==6 CALL :MERGE "%~5" "%~6"

FOR %%M IN (1 3 4 6) DO IF %MASK%==%%M CALL :INPUT
IF %MASK%==6 CALL :MERGE %7 %8
FOR %%N IN (2 3 5 6) DO IF %MASK%==%%N CALL :OUTPUT
GOTO:EOF

:INPUT_ONLY
SET INPUT_XML=%1
IF NOT EXIST %INPUT_XML% CALL :QUIT checkpaths
GOTO:EOF

:OUTPUT_ONLY
SET OUTPUT_XML=%1
IF NOT EXIST %OUTPUT_XML% CALL :QUIT checkpaths
GOTO:EOF

:BOTH
SET INPUT_XML=%1
IF NOT EXIST %INPUT_XML% CALL :QUIT checkpaths
SET OUTPUT_XML=%2
IF NOT EXIST %OUTPUT_XML% CALL :QUIT checkpaths
GOTO:EOF

:MERGE
SET FILTRE=%2
ECHO MERGE
CD "%1"
SET /A Compteur=0
FOR /r %%Z IN (%FILTRE%) DO CALL :COPY "%%Z" %FILTRE%
CD %~dp0
SET INPUT_XML=%INPUT_FOLDER%\TEMP
SET OUTPUT_XML=%INPUT_FOLDER%\TEMP
IF NOT EXIST %TEMP% CALL :QUIT checkpaths
GOTO:EOF

:COPY
ECHO COPY
SET /A Compteur+=1
SET TEMP=%INPUT_FOLDER%\TEMP
ECHO F | CALL "%~dp0/../Tools/xcopy.exe" /C /I /Q "%~1" "%TEMP%\temp%Compteur%\%FILTRE%" > nul
GOTO:EOF

:INPUT
ECHO INPUT
set DIR=input
SET PREFIX=i_
FOR %%P IN (1 3) DO IF %XSD_TYPE%==%%P CALL :CREATE_XSD "%INPUT_XML%"
CALL :LOCATE_XSD
CALL :BUILD_JAR
IF EXIST %TEMP% RD /S /Q %TEMP% > nul
GOTO:EOF

:OUTPUT
set DIR=output
SET PREFIX=o_
FOR %%Q IN (2 3) DO IF %XSD_TYPE%==%%Q CALL :CREATE_XSD "%OUTPUT_XML%"
CALL :LOCATE_XSD
CALL :BUILD_JAR
IF EXIST %TEMP% RD /S /Q %TEMP% > nul
GOTO:EOF

REM ## XSD Generation
:CREATE_XSD
echo.
echo ==============================
echo Generating %DIR% XSD file
echo ==============================
echo.
"%JAVA_HOME%\bin\java.exe" -Xms%XMS%m -Xmx%XMX%m -classpath "%cp%" org.apache.xmlbeans.impl.inst2xsd.Inst2Xsd %* -design ss -enumerations %ENUM% -outDir xsd\%VERSION%\%DIR%\ -outPrefix %NAME% -validate "%~1"
GOTO:EOF

REM ## Locating XSD, preparing JAR generation
:LOCATE_XSD
SET XSD=xsd/%VERSION%/%DIR%/%NAME%.xsd
IF NOT EXIST %XSD% SET XSD=xsd/%VERSION%/%DIR%/%NAME%0.xsd
IF NOT EXIST %XSD% ( CALL :QUIT cannot_find_xsd ) ELSE ( GOTO:EOF )

REM ## JAR Generation
:BUILD_JAR
echo.
echo ============================
echo Building %DIR% JAR file
echo ============================
echo.
set BINDING=xsd/%VERSION%/%DIR%/%NAME%_external_binding.xml
IF EXIST %BINDING% ( SET PARAM=-b %BINDING% ) ELSE ( SET PARAM= )

"%JAVA_HOME%\bin\java.exe" -jar libs\jaxb-xjc.jar -extension %PARAM% -p com.parser.%DIR%.%GAME%.%NAME% %XSD%
"%JAVA_HOME%\bin\javac.exe" com\parser\%DIR%\%GAME%\%NAME%\*.java > nul
"%JAVA_HOME%\bin\jar.exe" cvf %PREFIX%%NAME%.jar com > nul & ECHO.
RMDIR /s /q com
CALL "%~dp0..\Tools\xcopy.exe" /C /I /Q /Y %PREFIX%%NAME%.jar "%SOURCES%\libs\%DIR%\" > nul
IF EXIST "%PREFIX%%NAME%.jar" DEL /Q "%PREFIX%%NAME%.jar" > nul
GOTO:EOF

:FINISHED
echo.
echo ==============================================
echo Every operation was done successfully !
echo You may now Build the parser.
echo ==============================================
echo.
IF EXIST "%TEMP%" RD /S /Q "%TEMP%" > nul
pause
GOTO QUIT2

:QUIT
IF "%1"=="wrong_game" ECHO Error, wrong game selected, exiting !
IF "%1"=="checkpaths" ECHO Error in checkpaths !
IF "%1"=="folder" ECHO Error in folder's path definition !
IF "%1"=="cannot_find_xsd" ECHO Error, could not find XSD !

IF EXIST "%TEMP%" RD /S /Q "%TEMP%" > nul
pause
exit

:QUIT2