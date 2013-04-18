@ECHO OFF

:: NAME MASK XMS XMX INPUT [INPUT_FILTER] OUTPUT [OUTPUT_FILTER]
SET NAME=%~1
SET MASK=%2
SET XMS=%3
SET XMX=%4
SET FILTRE=

IF %XSD_TYPE%==1 SET INPUT_XML="%~5"
IF %XSD_TYPE%==2 SET OUTPUT_XML="%~5"
IF %XSD_TYPE%==3 (
	SET INPUT_XML="%~5"
	SET OUTPUT_XML="%~6"
)

IF NOT %XSD_TYPE%==0 FOR %%V IN (3 4 5 6) DO IF %MASK%==%%V CALL :MERGE %~5 %~6

FOR %%M IN (1 3 4 6) DO IF %MASK%==%%M CALL :INPUT
IF %MASK%==6 CALL :MERGE %7 %8
FOR %%N IN (2 3 5 6) DO IF %MASK%==%%N CALL :OUTPUT
GOTO FINISHED

:MERGE
SET FILTRE=%2
ROBOCOPY  %~1 %TEMP% /R:1 /W:1 /S %FILTRE% > nul
SET INPUT_XML=%TEMP%
SET OUTPUT_XML=%TEMP%
IF NOT EXIST %TEMP% CALL Fail.bat checkpaths
GOTO:EOF

:INPUT
set DIR=input
SET PREFIX=i_
FOR %%P IN (1 3) DO IF %XSD_TYPE%==%%P (
	IF NOT EXIST %INPUT_XML% CALL Fail.bat checkpaths
	CALL :CREATE_XSD "%INPUT_XML%"
)
CALL :LOCATE_XSD
CALL :BUILD_JAR
IF EXIST "%TEMP%" RD /S /Q "%TEMP%" > nul
GOTO:EOF

:OUTPUT
set DIR=output
SET PREFIX=o_
FOR %%Q IN (2 3) DO IF %XSD_TYPE%==%%Q (
	IF NOT EXIST %OUTPUT_XML% CALL Fail.bat checkpaths
	CALL :CREATE_XSD "%OUTPUT_XML%"
)
CALL :LOCATE_XSD
CALL :BUILD_JAR
IF EXIST "%TEMP%" RD /S /Q "%TEMP%" > nul
GOTO:EOF

:: XSD Generation
:CREATE_XSD
echo.
echo ==============================
echo Generating %DIR% XSD file
echo ==============================
echo.
"%JAVA_HOME%\bin\java.exe" -Xms%XMS%m -Xmx%XMX%m -classpath "%cp%" org.apache.xmlbeans.impl.inst2xsd.Inst2Xsd %* -design ss -enumerations %ENUM% -outDir %XSD_DIR%\%VERSION%\%DIR%\ -outPrefix %NAME% -validate "%~1" > nul & ECHO Done !
GOTO:EOF

:: Locating XSD, preparing JAR generation
:LOCATE_XSD
IF EXIST %XSD_DIR%\%VERSION%\%DIR%\%NAME%.xsd ( SET XSD=%XSD_DIR%\%VERSION%\%DIR%\%NAME%.xsd ) ELSE ( SET XSD=%XSD_DIR%/%VERSION%/%DIR%/%NAME%0.xsd )
IF NOT EXIST %XSD% ( CALL Fail.bat cannot_find_xsd ) ELSE ( GOTO:EOF )

:: JAR Generation
:BUILD_JAR
echo.
echo ============================
echo Building %DIR% JAR file
echo ============================
echo.
set BINDING=%XSD_DIR%\%VERSION%\%DIR%\%NAME%_external_binding.xml
IF EXIST %BINDING% ( SET PARAM=-b %BINDING% ) ELSE ( SET PARAM= )

"%JAVA_HOME%\bin\java.exe" -jar %LIBS%\jaxb-xjc.jar -extension %PARAM% -p com.parser.%DIR%.%GAME%.%NAME% %XSD%
"%JAVA_HOME%\bin\javac.exe" com\parser\%DIR%\%GAME%\%NAME%\*.java > nul
"%JAVA_HOME%\bin\jar.exe" cvf %PREFIX%%NAME%.jar com > nul & ECHO.
RMDIR /s /q com
CALL %XCOPY% /C /I /Q /Y %PREFIX%%NAME%.jar "%SOURCES%\libs\%DIR%\" > nul
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

CALL Jar_Jar_Bean.bat