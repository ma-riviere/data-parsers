@ECHO OFF
COLOR 4

:MENU
ECHO.
ECHO #####################################################
ECHO ###      What Data do you want to parse ?         ###
ECHO #####################################################
ECHO ======    Client XML build (customization) :   ======
ECHO # a : Items        ###
ECHO #####################################################
ECHO ======   Internal XML build (faster parsing) :  ======
ECHO # A : Item ID - Item Name                         ###
ECHO #####################################################
ECHO ======        Server XML build  :       =======
ECHO # 1 : Rides        ###    2 : Instances Cooltimes ###
ECHO # 3 : Items        ###    4 : Recipes             ###
ECHO # 5 : Skills       ###                            ###
ECHO #####################################################
ECHO ======        Server Custom XML build  :       ======
ECHO # / : KS Rewards   ###    
ECHO #####################################################
ECHO # q : Quit
ECHO #####################################################
ECHO.
SET CHOICE=
SET /P CHOICE=What do you want to parse : %=%

SET Start=

REM Client
IF "%CHOICE%"=="a" SET Start=items.AionClientItemsStart

REM Internal
IF "%CHOICE%"=="A" SET Start=items.AionItemsInternalStart

REM Server
IF "%CHOICE%"=="1" SET Start=AionRidesStart
IF "%CHOICE%"=="2" SET Start=AionCooltimesStart
IF "%CHOICE%"=="3" SET Start=items.AionItemsStart
IF "%CHOICE%"=="4" SET Start=AionRecipesStart
IF "%CHOICE%"=="5" SET Start=AionSkillsStart

REM Custom


REM Quit
IF "%CHOICE%"=="q" GOTO QUIT
IF "%CHOICE%"=="" GOTO QUIT

JAVA -Xms512m -Xmx1024m -ea -cp libs/*;Parser.jar com.parser.start.aion.%Start%

GOTO MENU

:QUIT