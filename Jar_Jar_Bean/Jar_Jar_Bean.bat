@ECHO OFF
TITLE [ Jar Jar Bean ]
COLOR 05

PUSHD %~dp0

SET INPUT_FOLDER="%~dp0..\Input"
SET OUTPUT_FOLDER="%~dp0..\Output"
SET SOURCES="%~dp0..\Sources"
SET LIBS=libs
SET BATCH=%~dp0batch
SET cp=%LIBS%\xbean.jar;%LIBS%\xmlbeans-qname.jar;%LIBS%\jsr173_1.0_api.jar;%LIBS%\resolver.jar

SET MAIN=%BATCH%\Main.bat
SET FAIL=%BATCH%\Fail.bat

:MENU
CALL :GAME_MENU
CALL :SET_PATHWAYS
CALL :XSD_MENU
FOR %%A IN (1 2 3) DO IF %XSD_TYPE%==%%A ( CALL :ENUM_MENU ) ELSE ( SET ENUM=never )
CALL :EXECUTE
GOTO QUIT

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

IF NOT EXIST %CLIENT% CALL %FAIL% folder
IF NOT EXIST %SERVER% CALL %FAIL% folder

REM ## Specific pathways
IF %GAME%==aion CALL "%BATCH%\Aion_Pathways.bat"
IF %GAME%==archeage CALL "%BATCH%\Archeage_Pathways.bat"
IF %GAME%==tera CALL "%BATCH%\Tera_Pathways.bat"
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

:EXECUTE
IF %GAME%==aion CALL "%BATCH%\Aion_Menu.bat"
IF %GAME%==archeage CALL "%BATCH%\Archeage_Menu.bat"
IF %GAME%==tera CALL "%BATCH%\Tera_Menu.bat"
GOTO:EOF

:QUIT