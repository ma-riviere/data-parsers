@ECHO OFF
TITLE [ Jar Jar Bean ]
COLOR 05

PUSHD %~dp0

SET INPUT_FOLDER=%~dp0..\Data
SET LIBS=libs
SET SOURCES=%~dp0..\Sources\src

SET cp=%LIBS%\xbean.jar;%LIBS%\xmlbeans-qname.jar;%LIBS%\jsr173_1.0_api.jar;%LIBS%\resolver.jar

:MENU
CALL :GAME_MENU
REM IF NOT EXIST %GAME% CALL :QUIT wrong_game
CALL :SET_PATHWAYS
CALL :JAR_MENU
CALL :XSD_MENU
FOR %%A IN (1 2 3) DO IF %XSD_TYPE%==%%A ( CALL :ENUM_MENU ) ELSE ( SET ENUM=never )
REM ## Start

IF %JAR%==10 CALL :MAIN animations 1 800 1024 %I_ANIMATIONS%
IF %JAR%==15 CALL :MAIN level_data 4 800 1024 %I_LEVELS% leveldata.xml

REM ## End
GOTO FINISHED

:GAME_MENU
ECHO #####################################################
ECHO ###      What VERSION would you like to parse ?   ###
ECHO #####################################################
ECHO #    1 : Aion 3.5      ###      2 : Aion 4.0      ###
ECHO #####################################################
ECHO.
set /P VERSION=[VERSION] Enter your choice : %=%

FOR %%B IN (1 2) DO IF %VERSION%==%%B SET GAME=aion
FOR %%C IN (3 4) DO IF %VERSION%==%%C SET GAME=tera

IF %VERSION%==1 SET VERSION=aion35
IF %VERSION%==2 SET VERSION=aion40
GOTO:EOF

:SET_PATHWAYS
REM ## General pathways
SET CLIENT=%INPUT_FOLDER%\%VERSION%\client
SET SERVER=%INPUT_FOLDER%\%VERSION%\server
SET MAPPINGS=%INPUT_FOLDER%\Mappings
SET TESTS=%INPUT_FOLDER%\Tests

IF NOT EXIST %CLIENT% CALL :QUIT folder
IF NOT EXIST %SERVER% CALL :QUIT folder
IF NOT EXIST %MAPPINGS% CALL :QUIT folder
REM ## Specific pathways
IF %GAME%==aion CALL :AION_PATHWAYS
IF %GAME%==tera CALL :TERA_PATHWAYS
GOTO:EOF

:AION_PATHWAYS
SET I_ANIMATIONS="%CLIENT%\Data\Animations\custom_animation.xml"
SET I_LEVELS="%CLIENT%\Levels\"
GOTO:EOF

:JAR_MENU
ECHO.
ECHO #####################################################
ECHO ###      What JAR would you like to compile :     ###
ECHO #####################################################
ECHO ======    Client XML build (customization) :   ======
ECHO =====================================================
ECHO = a : Items        ### b : Strings                ===
ECHO =====================================================
ECHO ======  Internal XML build (faster parsing) :  ======
ECHO = A : Item (ID-Name)                              ===
ECHO =====================================================
ECHO ======           Server XML build :            ======
ECHO =====================================================
ECHO = 1 : Toypets      ===    2 : World Maps          ===
ECHO = 3 : Rides        ===    4 : Instances Cooltimes ===
ECHO = 5 : Items        ===    6 : Recipes             ===
ECHO = 7 : Skills       ===    8 : Skill Learn         ===
ECHO = 9 : Npcs         ===   10 : Animations          ===
ECHO = 11 : Housing     ===   12 : Mission0            ===
ECHO = 13 : World Data  ===   14 : Walkers             ===
ECHO = 15 : Level Data  ===                            ===
ECHO.#####################################################
ECHO.
set /P JAR=[JAR] JAR file to compile : %=%
GOTO:EOF

:XSD_MENU
ECHO.
ECHO ##########################################################
ECHO ###       Which XSD would you like to generate :       ###
ECHO ##########################################################
ECHO # 0 : Do not generate any .xsd files, use existing ones. #
ECHO # 1 : Generate a new .xsd for the INPUT .xml.            #
ECHO # 2 : Generate a new .xsd for the OUTPUT .xml.           #
ECHO # 3 : Generate new .xsd files for INPUT and OUTPUT .xml  #
ECHO ##########################################################
ECHO.
SET /P XSD_TYPE=[XSD GEN] XSD to generate : %=%
GOTO:EOF

:ENUM_MENU
ECHO.
ECHO #########################################################
ECHO ###    How many ENUMERATIONS should be generated ?    ###
ECHO #########################################################
ECHO.
SET /P ENUM=[ENUM] Number of ENUMERATIONS to generate : %=%
GOTO:EOF

:MAIN
REM ## NAME MASK XMS XMX INPUT OUTPUT FILENAME
SET NAME=%1
SET MASK=%2
SET XMS=%3
SET XMX=%4
SET FILTRE=

IF %MASK%==1 CALL :INPUT_ONLY %5
IF %MASK%==2 CALL :OUTPUT_ONLY %5
IF %MASK%==3 CALL :BOTH %5 %6
IF %MASK%==4 CALL :MERGE %5 %6
IF %MASK%==5 CALL :MERGE %5 %6
ECHO AFTER
FOR %%M IN (1 3 4) DO IF %MASK%==%%M ( CALL :INPUT )
FOR %%N IN (2 3 5) DO IF %MASK%==%%N ( CALL :OUTPUT )
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
CD "%1"
SET /A Compteur=0
FOR /r %%Z IN (%2) DO CALL :COPY "%%Z" %FILTRE%
CD %~dp0
SET INPUT_XML=%INPUT_FOLDER%\TEMP
SET OUTPUT_XML=%INPUT_FOLDER%\TEMP
IF NOT EXIST %TEMP% CALL :QUIT checkpaths
GOTO:EOF

:COPY
SET /A Compteur+=1
SET TEMP=%INPUT_FOLDER%\TEMP
ECHO F | CALL "%~dp0/../Tools/xcopy.exe" /C /I /Q %1 "%TEMP%\temp%Compteur%\%2" > nul
GOTO:EOF

:INPUT
set DIR=input
SET PREFIX=i_
FOR %%P IN (1 3) DO IF %XSD_TYPE%==%%P CALL :Create_xsd "%INPUT_XML%"
CALL :Locate_xsd
CALL :Build_jar
GOTO:EOF

:OUTPUT
set DIR=output
SET PREFIX=o_
FOR %%Q IN (2 3) DO IF %3==%%Q CALL :Create_xsd "%OUTPUT_XML%"
CALL :Locate_xsd
CALL :Build_jar
GOTO:EOF

REM ## XSD Generation
:Create_xsd
echo.
echo ==============================
echo Generating %DIR% XSD file
echo ==============================
echo.
ECHO %NAME%
ECHO %DIR%
ECHO %VERSION%
ECHO %1
"%JAVA_HOME%\bin\java.exe" -Xms%XMS%m -Xmx%XMX%m -classpath "%cp%" org.apache.xmlbeans.impl.inst2xsd.Inst2Xsd %* -design ss -enumerations %ENUM% -outDir xsd\%VERSION%\%DIR%\ -outPrefix %NAME% -validate %1
GOTO:EOF

REM ## Locating XSD, preparing JAR generation
:Locate_xsd
ECHO LOCATE
SET XSD=xsd/%VERSION%/%DIR%/%NAME%.xsd
IF NOT EXIST %XSD% SET XSD=xsd/%VERSION%/%DIR%/%NAME%0.xsd
IF NOT EXIST %XSD% ( CALL :QUIT cannot_find_xsd ) ELSE ( GOTO:EOF )

REM ## JAR Generation
:Build_jar
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
CALL "%~dp0\..\Tools\xcopy.exe" /C /I /Q /Y %PREFIX%%NAME%.jar "%~dp0\..\Sources\libs\%DIR%\%PREFIX%%NAME%.jar" > nul & ECHO Done !
DEL /Q %PREFIX%%NAME%.jar
GOTO:EOF

:FINISHED
echo.
echo ==============================================
echo Every operation was done successfully !
echo You may now Build the parser.
echo ==============================================
echo.
RD /S /Q %TEMP% > nul
pause
exit

:QUIT
IF "%1"=="wrong_game" ECHO Error, wrong game selected, exiting !
IF "%1"=="checkpaths" ECHO Error in checkpaths !
IF "%1"=="folder" ECHO Error in folder's path definition !
IF "%1"=="cannot_find_xsd" ECHO Error, could not find XSD !

RD /S /Q %TEMP% > nul
pause
exit