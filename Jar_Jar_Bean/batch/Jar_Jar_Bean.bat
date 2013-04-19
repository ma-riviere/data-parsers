@ECHO OFF
TITLE [ Jar Jar Bean ]
COLOR 05

PUSHD "..\..\"

SET INPUT_FOLDER=%CD%\Input
SET OUTPUT_FOLDER=%CD%\Output
SET SOURCES=%CD%\Sources
SET LIBS="%CD%\Jar_Jar_Bean\libs"
SET XSD_DIR="%CD%\Jar_Jar_Bean\xsd"
SET TEMP="%INPUT_FOLDER%\TEMP"
SET XCOPY="%CD%\Tools\xcopy.exe"
SET cp=%LIBS%\xbean.jar;%LIBS%\xmlbeans-qname.jar;%LIBS%\jsr173_1.0_api.jar;%LIBS%\resolver.jar

POPD

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

IF NOT EXIST %CLIENT% CALL Fail.bat folder
IF NOT EXIST %SERVER% CALL Fail.bat folder

REM ## Specific pathways
IF %GAME%==aion CALL Aion_Pathways.bat
IF %GAME%==archeage CALL Archeage_Pathways.bat
IF %GAME%==tera CALL Tera_Pathways.bat
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
IF %ENUM%==0 SET ENUM=never
GOTO:EOF

:EXECUTE
IF %GAME%==aion CALL Aion_Menu.bat
IF %GAME%==archeage CALL Archeage_Menu.bat
IF %GAME%==tera CALL Tera_Menu.bat
GOTO:EOF

:QUIT