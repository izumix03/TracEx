set EXEC_DIR=%~dp0
echo %EXEC_DIR%
set INSTALL_PATH=%EXEC_DIR%TracEx.exe
set CLASSPATH=%EXEC_DIR%trac-ex.jar;
set JVM_PATH="C:\jdk\jdk1.7.0_25\jre\bin\server\jvm.dll"

TracEx //IS//TracEx^
 --DisplayName="TracEx"^
 --Install=%INSTALL_PATH%^
 --Startup=auto^
 --Jvm=%JVM_PATH%^
 --StartMode=jvm^
 --StopMode=jvm^
 --Classpath=%CLASSPATH%^
 --StartClass=iz.tracex.TracExService^
 --StartMethod=startService^
 --StopClass=iz.tracex.TracExService^
 --StopMethod=stopService^
 --LogPath=%EXEC_DIR%logs^
 --LogLevel=DEBUG^
 --StdOutput=auto^
 --StdError=auto
