@ECHO OFF
TITLE [ Jar Jar Bean ]

REM SETX /M JAVA_HOME "C:\Program Files\Java\jdk1.7.0_17" > nul
SETX /M JAVA_HOME "C:\Program Files\Java\jdk1.8.0" > nul

CD Jar_Jar_Bean\batch
CALL "Jar_Jar_Bean.bat"