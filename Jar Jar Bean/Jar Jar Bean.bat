@ECHO off
TITLE Jar Jar Bean
COLOR 05

setx /M JAVA_HOME "C:\Program Files (x86)\Java\jdk1.7.0_05"
REM setx /M JAVA_HOME "C:\Program Files\Java\jdk1.7.0_17"

SET CURRENT=
SET PREFIX=
SET DIR=null
SET VERSION=0
SET XMS=800
SET XMX=1024
SET SOURCES=../Sources/src

REM ## NetBeans XSD generator params
set XMLBEANS_LIB=libs
set cp=%XMLBEANS_LIB%\xbean.jar;%XMLBEANS_LIB%\xmlbeans-qname.jar;%XMLBEANS_LIB%\jsr173_1.0_api.jar;%XMLBEANS_LIB%\resolver.jar

ECHO #####################################################
ECHO ###    What VERSION would you like to parse ?     ###
ECHO #####################################################
ECHO #    1 : Aion 3.5      ###      2 : Aion 4.0      ###
ECHO #####################################################
ECHO.
set CHOICE=
set /P CHOICE=[VERSION] Enter your choice : %=%

IF "%CHOICE%"=="1" SET VERSION=aion35
IF "%CHOICE%"=="2" SET VERSION=aion40

REM ## Setting Client Destination Folder
set CLIENT=..\Data\%VERSION%\client
set SERVER=..\Data\%VERSION%\server
set MAPPINGS=..\Data\Mappings
IF NOT EXIST %CLIENT% GOTO CLIENT_PATH_ERROR
IF NOT EXIST %SERVER% GOTO CLIENT_PATH_ERROR

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
ECHO = 11 : Housing     ===                            ===
ECHO.#####################################################
ECHO.
set JAR =
set /P JAR=[JAR] JAR file to compile : %=%

set NAME=
set INPUT_XML=
set OUTPUT_XML=

REM ## Client Data pathways
SET DATA_STRINGS=%CLIENT%/Data/Strings/client_strings*.xml
SET L10N_STRINGS=%CLIENT%/L10N/ENU/data/strings/client_strings*.xml
SET WORLD=%CLIENT%/Data/world/WorldId.xml
SET RIDES=%CLIENT%/Data/rides/rides.xml
SET COOLTIMES=%CLIENT%/Data/world/client_instance_cooltime*.xml
SET ITEMS=%CLIENT%/Data/Items/client_items_*.xml
SET RECIPES=%CLIENT%/Data/Items/client_combine_recipe.xml
SET SKILLS=%CLIENT%/Data/skills/client_skills.xml
SET SKILL_LEARN=%CLIENT%/Data/skills/client_skill_learns.xml
SET NPCS=%CLIENT%/Data/Npcs/client_npcs.xml
SET ANIMATIONS=%CLIENT%/Data/Animations/custom_animation.xml
SET HOUSING=%CLIENT%/Data/Housing/client_housing*.xml
SET TOYPETS=%CLIENT%/Data/func_pet/toypet*.xml

FOR %%A IN (a b c d e f g h) DO IF %JAR%==%%A GOTO CLIENT_CLIENT
FOR %%A IN (A B C D E F G H) DO IF %JAR%==%%A GOTO CLIENT_PARSER
FOR %%A IN (1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18) DO IF %JAR%==%%A GOTO CLIENT_SERVER

:CLIENT_CLIENT
REM ## Imput xsd = Output xsd
ECHO.
ECHO [INFO] Client-Client parsing selected

SET DIR=client
SET SAME=true
IF "%JAR%"=="a" GOTO CLIENT_ITEMS
IF "%JAR%"=="b" GOTO CLIENT_STRINGS

:CLIENT_PARSER
ECHO.
ECHO [INFO] Client-Parser parsing selected

SET DIR=parser
SET SAME=true
IF "%JAR%"=="A" GOTO PARSER_ITEMS

:CLIENT_SERVER
ECHO.
ECHO [INFO] Client-Server parsing selected

SET SAME=false

IF "%JAR%"=="1" GOTO TOYPETS
IF "%JAR%"=="2" GOTO WORLD
IF "%JAR%"=="3" GOTO RIDES
IF "%JAR%"=="4" GOTO COOLTIMES
IF "%JAR%"=="5" GOTO ITEMS
IF "%JAR%"=="6" GOTO RECIPES
IF "%JAR%"=="7" GOTO SKILLS
IF "%JAR%"=="8" GOTO SKILL_LEARN
IF "%JAR%"=="9" GOTO NPCS
IF "%JAR%"=="10" GOTO ANIMATIONS
IF "%JAR%"=="11" GOTO HOUSING

:CLIENT_ITEMS
set XMS=1024
set XMX=1536
set NAME=items
set INPUT_XML=%ITEMS%
set OUTPUT_XML=%INPUT_XML%
GOTO CHECKPATHS_XML

:CLIENT_STRINGS
REM ## TODO : Changer l'ordre des XML quand la 4.0 sera traduite
set NAME=strings
set INPUT_XML=%DATA_STRINGS%
IF NOT EXIST %INPUT_XML% set INPUT_XML=%L10N_STRINGS%
set OUTPUT_XML=%INPUT_XML%
GOTO CHECKPATHS_XML

REM ## Client - Parser (Internal, for faster parsing)
:PARSER_ITEMS
set NAME=p_items
set INPUT_XML=%MAPPINGS%/items_name_id.xml
set OUTPUT_XML=%INPUT_XML%
GOTO CHECKPATHS_XML

REM ## Client - Server
:ITEMS
set XMS=512
set XMX=1024
set NAME=items
set INPUT_XML=%ITEMS%
set OUTPUT_XML=%SERVER%/item_templates.xml
GOTO CHECKPATHS_XML

:WORLD
set NAME=world
set INPUT_XML=%WORLD%
set OUTPUT_XML=%SERVER%/world_maps.xml
GOTO CHECKPATHS_XML

:RIDES
set NAME=rides
set INPUT_XML=%RIDES%
set OUTPUT_XML=%SERVER%/ride.xml
GOTO CHECKPATHS_XML

:COOLTIMES
set NAME=cooltimes
set INPUT_XML=%COOLTIMES%
set OUTPUT_XML=%SERVER%/instance_cooltimes.xml
GOTO CHECKPATHS_XML

:SKILLS
set XMS=800
set XMX=1200
set NAME=skills
set INPUT_XML=%SKILLS%
set OUTPUT_XML=%SERVER%/skills/skill_templates.xml
GOTO CHECKPATHS_XML

:SKILL_LEARN
set NAME=skill_learn
set INPUT_XML=%SKILL_LEARN%
set OUTPUT_XML=%SERVER%/skills/skill_tree.xml
GOTO CHECKPATHS_XML

:RECIPES
set NAME=recipes
set INPUT_XML=%RECIPES%
set OUTPUT_XML=%SERVER%/recipe_templates.xml
GOTO CHECKPATHS_XML

:NPCS
set XMS=1024
set XMX=1536
set NAME=npcs
set INPUT_XML=%NPCS%
set OUTPUT_XML=%SERVER%/npc_templates.xml
GOTO CHECKPATHS_XML

:HOUSING
set NAME=housing
set INPUT_XML=%HOUSING%
set OUTPUT_XML=%SERVER%/housing/*.xml
GOTO CHECKPATHS_XML

:TOYPETS
set NAME=toypets
set INPUT_XML=%TOYPETS%
set OUTPUT_XML=%SERVER%/toypets/*.xml
GOTO CHECKPATHS_XML

:ANIMATIONS
set NAME=animations
set INPUT_XML=%ANIMATIONS%
IF NOT EXIST %INPUT_XML% GOTO CLIENT_PATH_ERROR
GOTO XSD_TYPE

REM ## END

:CHECKPATHS_XML
IF NOT EXIST %INPUT_XML% GOTO QUIT_ABNORMAL
IF NOT EXIST %OUTPUT_XML% GOTO QUIT_ABNORMAL

:XSD_TYPE
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
set XSD_TYPE =
set /P XSD_TYPE=[XSD GEN] XSD to generate : %=%

set ENUM=never
IF "%XSD_TYPE%"=="0" GOTO INPUT

:ENUMERATIONS
ECHO.
ECHO #########################################################
ECHO ###    How many ENUMERATIONS should be generated ?    ###
ECHO #########################################################
ECHO.
set /P ENUM=[ENUM] Number of ENUMERATIONS to generate : %=%

IF "%ENUM%"=="0" SET ENUM=never

REM ## Input
:INPUT
set CURRENT=input
SET PREFIX=i_
IF "%DIR%"=="null" SET DIR=client
SET XML=%INPUT_XML%
IF "%XSD_TYPE%"=="0" GOTO INIT_XSD
IF "%XSD_TYPE%"=="2" GOTO INIT_XSD
GOTO GENERATE_XSD

REM ## Output
:OUTPUT
set CURRENT=output
SET PREFIX=o_
SET DIR=server
SET XML=%OUTPUT_XML%
IF "%XSD_TYPE%"=="0" GOTO INIT_XSD
IF "%XSD_TYPE%"=="1" GOTO INIT_XSD
GOTO GENERATE_XSD


REM ## XSD Generation
:GENERATE_XSD
echo.
echo ==============================
echo Generating %CURRENT% XSD file
echo ==============================
echo.

java -Xms%XMS%m -Xmx%XMX%m -classpath "%cp%" org.apache.xmlbeans.impl.inst2xsd.Inst2Xsd %* -design ss -enumerations %ENUM% -outDir xsd\%VERSION%\%DIR%\ -outPrefix %NAME% -validate %XML% > nul && ECHO Done !
GOTO INIT_XSD

REM ## Locating XSD, preparing JAR generation
:INIT_XSD
SET XSD=xsd/%VERSION%/%DIR%/%NAME%.xsd
IF NOT EXIST %XSD% SET XSD=xsd/%VERSION%/%DIR%/%NAME%0.xsd
IF NOT EXIST %XSD% GOTO QUIT_ABNORMAL
GOTO BUILD_JAR

REM ## JAR Generation
:BUILD_JAR
echo.
echo ============================
echo Building %CURRENT% JAR file
echo ============================
echo.
set BINDING=xsd/%VERSION%/%DIR%/%NAME%_external_binding.xml
SET PARAM=
IF EXIST %BINDING% SET PARAM=-b %BINDING%

IF "%DIR%"=="parser" SET PREFIX=

java -jar libs\jaxb-xjc.jar -extension %PARAM% -p com.parser.%CURRENT%.aion.%NAME% %XSD%
"%JAVA_HOME%\bin\javac.exe" com\parser\%CURRENT%\aion\%NAME%\*.java > nul
"%JAVA_HOME%\bin\jar.exe" cvf %PREFIX%%NAME%.jar com > nul & ECHO.
rmdir /s /q com
ROBOCOPY . ..\Sources\libs\%DIR%\ %PREFIX%%NAME%.jar /MOV /NJS /NJH > nul & ECHO Done !

IF "%SAME%"=="true" GOTO QUIT
IF "%CURRENT%"=="input" IF "%DIR%"=="client" GOTO OUTPUT
GOTO QUIT

:CLIENT_PATH_ERROR
echo.
echo.
echo =======================================================
echo Input/Output folder could not be found ...
echo Please check the name/location of client/server files.
echo =======================================================
echo.
pause.
exit

:QUIT_ABNORMAL
echo.
echo.
echo ==============================================
echo XSD/XML files could not be found ...
echo Please check the name/location of xsd files.
echo ==============================================
echo.
pause.
exit

:QUIT
echo.
echo.
echo ==============================================
echo Every operation was done successfully !
echo You may now Build the parser.
echo ==============================================
echo.
pause.