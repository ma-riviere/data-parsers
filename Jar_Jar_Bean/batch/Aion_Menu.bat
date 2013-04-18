@ECHO OFF

ECHO.
ECHO #####################################################
ECHO ###      What JAR would you like to compile :     ###
ECHO #####################################################
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
ECHO = 15 : Level Data  ===   16 : Strings             ===
ECHO.#####################################################
ECHO.
set /P JAR=[JAR] JAR file to compile : %=%

REM ## Data
IF %JAR%==1 CALL Main.bat toypets 3 800 1024 %I_TOYPETS% %O_TOYPETS%
IF %JAR%==2 CALL Main.bat world_maps 3 800 1024 %I_WORLD_MAPS% %O_WORLD_MAPS%
IF %JAR%==3 CALL Main.bat rides 3 800 1024 %I_RIDES% %O_RIDES%
IF %JAR%==4 CALL Main.bat cooltimes 3 800 1024 %I_COOLTIMES% %O_COOLTIMES%
IF %JAR%==5 CALL Main.bat items 3 1024 1536 %I_ITEMS% %O_ITEMS%
IF %JAR%==6 CALL Main.bat recipes 3 800 1024 %I_RECIPES% %O_RECIPES%
IF %JAR%==7 CALL Main.bat skills 3 1000 1200 %I_SKILLS% %O_SKILLS%
IF %JAR%==8 CALL Main.bat skill_learn 3 800 1024 %I_SKILL_TREE% %O_SKILL_TREE%
IF %JAR%==9 CALL Main.bat npcs 1 1024 1536 %I_NPCS%
IF %JAR%==10 CALL Main.bat animations 1 800 1024 %I_ANIMATIONS%
IF %JAR%==11 CALL Main.bat housing 1 800 1024 %I_HOUSING%
IF %JAR%==13 CALL Main.bat world_data 1 800 1024 %I_WORLD_DATA%
IF %JAR%==14 CALL Main.bat walkers 2 800 1024 %O_WALKERS%
IF %JAR%==90 CALL Main.bat source_sphere 2 800 1024 %SOURCE_SPHERE%
IF %JAR%==91 CALL Main.bat height_map 2 800 1024 %HEIGHT_MAP%
IF %JAR%==A CALL Main.bat item_name 2 800 1024 %ITEMS_NAME%

REM ## Levels
IF %JAR%==12 CALL Main.bat mission 6 800 1024 %LEVELS% *mission0.xml %O_SPAWNS% *.xml
IF %JAR%==15 CALL Main.bat level_data 4 800 1024 %LEVELS% leveldata.xml