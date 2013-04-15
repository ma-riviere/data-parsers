@ECHO OFF
TITLE [ Jar Jar Bean ]
COLOR 05

PUSHD %~dp0

SET INPUT_FOLDER="%~dp0..\Input"
SET OUTPUT_FOLDER="%~dp0..\Output"
SET SOURCES="%~dp0..\Sources"
SET LIBS=libs

SET cp=%LIBS%\xbean.jar;%LIBS%\xmlbeans-qname.jar;%LIBS%\jsr173_1.0_api.jar;%LIBS%\resolver.jar

:MENU
CALL :GAME_MENU
CALL :SET_PATHWAYS
CALL :JAR_MENU
CALL :XSD_MENU
FOR %%A IN (1 2 3) DO IF %XSD_TYPE%==%%A ( CALL :ENUM_MENU ) ELSE ( SET ENUM=never )

REM ## Data
IF %JAR%==1 CALL :MAIN toypets 3 800 1024 %I_TOYPETS% %O_TOYPETS%
IF %JAR%==2 CALL :MAIN world_maps 3 800 1024 %I_WORLD_MAPS% %O_WORLD_MAPS%
IF %JAR%==3 CALL :MAIN rides 3 800 1024 %I_RIDES% %O_RIDES%
REM IF %JAR%==4 CALL :MAIN cooltimes
REM IF %JAR%==5 CALL :MAIN items
REM IF %JAR%==6 CALL :MAIN recipes
REM IF %JAR%==7 CALL :MAIN skills
REM IF %JAR%==8 CALL :MAIN skill_learn
REM IF %JAR%==9 CALL :MAIN npcs
IF %JAR%==10 CALL :MAIN animations 1 800 1024 %ANIMATIONS%
REM IF %JAR%==11 CALL :MAIN housing
REM IF %JAR%==13 CALL :MAIN world_data
REM IF %JAR%==14 CALL :MAIN walkers
IF %JAR%==90 CALL :MAIN source_sphere 2 800 1024 %SOURCE_SPHERE%
IF %JAR%==91 CALL :MAIN height_map 2 800 1024 %HEIGHT_MAP%
IF %JAR%==A CALL :MAIN item_name 2 800 1024 %ITEMS_NAME%

REM ## Levels
IF %JAR%==12 CALL :MAIN mission 6 800 1024 %LEVELS% *mission0.xml %SPAWNS% *.xml
IF %JAR%==15 CALL :MAIN level_data 4 800 1024 %LEVELS% leveldata.xml 

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
FOR %%B IN (3 4) DO IF %VERSION%==%%B SET GAME=archeage
FOR %%C IN (5 6) DO IF %VERSION%==%%C SET GAME=tera

IF %VERSION%==1 SET VERSION=aion35
IF %VERSION%==2 SET VERSION=aion40
GOTO:EOF

:SET_PATHWAYS
REM ## General pathways
SET CLIENT=%INPUT_FOLDER%\%VERSION%\Client
SET SERVER=%INPUT_FOLDER%\%VERSION%\Server
SET CUSTOM=%OUTPUT_FOLDER%\%VERSION%\Tests

IF NOT EXIST %CLIENT% CALL :QUIT folder
IF NOT EXIST %SERVER% CALL :QUIT folder
REM ## Specific pathways
IF %GAME%==aion CALL :AION_PATHWAYS
IF %GAME%==archeage CALL :ARCHEAGE_PATHWAYS
IF %GAME%==tera CALL :TERA_PATHWAYS
GOTO:EOF

:AION_PATHWAYS
SET I_TOYPETS=%CLIENT%\Data\func_pet\toypet*.xml
SET O_TOYPETS=%SERVER%\toypets\*.xml
SET ANIMATIONS=%CLIENT%\Data\Animations\custom_animation.xml

SET LEVELS=%CLIENT%\Levels\
SET SPAWNS=%SERVER%\spawns\

SET DATA_STRINGS=%CLIENT%\Data\Strings\client_strings*.xml
SET L10N_STRINGS=%CLIENT%\L10N\ENU\data\strings\client_strings*.xml
SET I_WORLD_MAPS=%CLIENT%\Data\world\WorldId.xml
SET WORLD_DATA=%CLIENT%\Data\world\client_world_*.xml
SET RIDES=%CLIENT%\Data\rides\rides.xml
SET COOLTIMES=%CLIENT%\Data\world\client_instance_cooltime*.xml
SET ITEMS=%CLIENT%\Data\Items\client_items_*.xml
SET RECIPES=%CLIENT%\Data\Items\client_combine_recipe.xml
SET SKILLS=%CLIENT%\Data\skills\client_skills.xml
SET SKILL_LEARN=%CLIENT%\Data\skills\client_skill_learns.xml
SET NPCS=%CLIENT%\Data\Npcs\client_npcs.xml
SET HOUSING=%CLIENT%\Data\Housing\client_housing*.xml

SET SOURCE_SPHERE=%CUSTOM%\source_sphere.xml
SET HEIGHT_MAP=%CUSTOM%\height_map.xml
SET ITEMS_NAME=%CUSTOM%\items_name_id.xml

REM SET QUEST_DIALOGS=%CLIENT%\L10N\ENU\data\dialogs\quest*.xml
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
REM ## NAME MASK XMS XMX INPUT [INPUT_FILTER] OUTPUT [OUTPUT_FILTER]
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
IF %MASK%==6 CALL :MERGE %5 %6

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
"%JAVA_HOME%\bin\java.exe" -Xms%XMS%m -Xmx%XMX%m -classpath "%cp%" org.apache.xmlbeans.impl.inst2xsd.Inst2Xsd %* -design ss -enumerations %ENUM% -outDir xsd\%VERSION%\%DIR%\ -outPrefix %NAME% -validate %1
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
IF EXIST %PREFIX%%NAME%.jar DEL /Q %PREFIX%%NAME%.jar > nul
GOTO:EOF

:FINISHED
echo.
echo ==============================================
echo Every operation was done successfully !
echo You may now Build the parser.
echo ==============================================
echo.
IF EXIST %TEMP% RD /S /Q %TEMP% > nul
pause
GOTO QUIT2

:QUIT
IF "%1"=="wrong_game" ECHO Error, wrong game selected, exiting !
IF "%1"=="checkpaths" ECHO Error in checkpaths !
IF "%1"=="folder" ECHO Error in folder's path definition !
IF "%1"=="cannot_find_xsd" ECHO Error, could not find XSD !

IF EXIST %TEMP% RD /S /Q %TEMP% > nul
pause
exit

:QUIT2