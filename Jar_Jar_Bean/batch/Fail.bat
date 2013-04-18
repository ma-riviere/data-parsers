@ECHO OFF

IF "%1"=="wrong_game" ECHO Error, wrong game selected, exiting !
IF "%1"=="checkpaths" ECHO Error in checkpaths !
IF "%1"=="folder" ECHO Error in folder's path definition !
IF "%1"=="cannot_find_xsd" ECHO Error, could not find XSD !

IF EXIST "%TEMP%" RD /S /Q "%TEMP%" > nul
pause
exit