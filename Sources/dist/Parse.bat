@ECHO OFF
COLOR 4

SET XMS=512
SET XMX=1024

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

REM Client
IF "%CHOICE%"=="a" CALL :EXECUTE items.AionClientItemsStart %XMS% %XMX%
REM Server
IF "%CHOICE%"=="1" CALL :EXECUTE AionRidesStart %XMS% %XMX%
IF "%CHOICE%"=="2" CALL :EXECUTE AionCooltimesStart %XMS% %XMX%
IF "%CHOICE%"=="3" CALL :EXECUTE items.AionItemsStart %XMS% %XMX%
IF "%CHOICE%"=="4" CALL :EXECUTE AionRecipesStart %XMS% %XMX%
IF "%CHOICE%"=="5" CALL :EXECUTE AionSkillsStart %XMS% %XMX%
IF "%CHOICE%"=="6" CALL :EXECUTE AionSpawnsStart 768 1536
IF "%CHOICE%"=="7" CALL :EXECUTE AionWalkersStart %XMS% %XMX%
REM Custom
IF "%CHOICE%"=="A" CALL :EXECUTE items.AionItemsInternalStart %XMS% %XMX%
IF "%CHOICE%"=="90" CALL :EXECUTE AionSourceSphereStart %XMS% %XMX%

GOTO MENU

:EXECUTE
JAVA -Xms%2m -Xmx%3m -ea -cp libs/*;Parser.jar com.parser.start.aion.%1 %ANALYSE%
GOTO:EOF

:QUIT