@ECHO OFF
COLOR 4

:MENU
CALL :GAME_MENU
ECHO.
CALL :DATA_MENU
ECHO.
CALL :JAXB_MENU
ECHO.
IF %GAME%==1 CALL :EXECUTE AionStart
IF %GAME%==2 CALL :EXECUTE TeraStart
IF %GAME%==3 CALL :EXECUTE ArcheageStart
GOTO MENU

:GAME_MENU
ECHO #####################################################
ECHO ###      What GAME would you like to parse ?      ###
ECHO #####################################################
ECHO ###  1 : Aion  ###   2 : Tera  ###  3 : Archeage  ###
ECHO #####################################################
ECHO.
SET /P GAME=What GAME do you want to parse : %=%
GOTO:EOF

:DATA_MENU
ECHO #####################################################
ECHO ###      What DATA would you like to parse ?      ###
ECHO #####################################################
ECHO.
SET /P DATA=What DATA do you want to parse : %=%
GOTO:EOF

IF "%DATA%"=="" GOTO QUIT

:JAXB_MENU
ECHO #####################################################
ECHO ###         List Client unused elements ?         ###
ECHO #####################################################
ECHO ###  0 : No            ###   1 : Yes              ###
ECHO #####################################################
ECHO.
SET /P ANALYSE=Do you want a list of client unused elements ? %=%

IF "%ANALYSE%"=="0" SET ANALYSE=false
IF "%ANALYSE%"=="1" SET ANALYSE=true

:EXECUTE
JAVA -Xms512m -Xmx1024m -ea -cp libs/*;Parser.jar com.parser.start.%1 %DATA% %ANALYSE%
GOTO:EOF

:QUIT