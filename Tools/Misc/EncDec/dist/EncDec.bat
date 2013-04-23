@ECHO OFF
set /P ORDER=[ORDER] Enter your order : %=%
set /P PASS=[PASS] Enter the password : %=%

java -cp ./libs/*;encdec.jar com.encdec.Banane %ORDER% %PASS%

pause