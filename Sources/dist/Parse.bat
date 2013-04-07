@ECHO OFF
COLOR 4

:MENU
ECHO.
ECHO #####################################################
ECHO ###      What Data do you want to parse ?         ###
ECHO #####################################################
ECHO ======    Client XML build (customization) :   ======
ECHO # a : Items        ###                            ###
ECHO #####################################################
ECHO ======  Internal XML build (faster parsing) :  ======
ECHO # A : Item ID - Item Name                         ###
ECHO #####################################################
ECHO ======        Server XML build  :             =======
ECHO # 1 : Rides        ###    2 : Instances Cooltimes ###
ECHO # 3 : Items        ###    4 : Recipes             ###
ECHO # 5 : Skills       ###    6 : Spawns              ###
ECHO # 7 : Walkers      ###                            ###
ECHO #####################################################
ECHO ======        Server Custom XML build  :       ======
ECHO # / : KS Rewards   ###                            ###
ECHO #####################################################
ECHO # [ ] : Quit                                      ###
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
IF "%CHOICE%"=="6" SET Start=AionSpawnsStart
IF "%CHOICE%"=="7" SET Start=AionWalkersStart

REM Tests
IF "%CHOICE%"=="90" SET Start=AionSourceSphereStart

REM Custom

REM Quit
IF "%CHOICE%"=="" GOTO QUIT

ECHO.   
ECHO #####################################################
ECHO ###         List Client unused elements ?         ###
ECHO #####################################################
ECHO ###  0 : No            ###   1 : Yes              ###
ECHO #####################################################
ECHO.
SET ANALYSE=
SET /P ANALYSE=Do you want a list of client unused elements ? %=%

IF "%ANALYSE%"=="0" SET ANALYSE=false
IF "%ANALYSE%"=="1" SET ANALYSE=true

JAVA -Xms512m -Xmx1024m -ea -cp libs/*;Parser.jar com.parser.start.aion.%Start% %ANALYSE%

GOTO MENU

:QUIT