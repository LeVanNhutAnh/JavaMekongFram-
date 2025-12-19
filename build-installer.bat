@echo off
REM ================================
REM Mekong Farm - Build Windows EXE
REM ================================

echo [1/4] Cleaning and building JAR...
call .\apache-maven-3.9.5\bin\mvn.cmd clean package -DskipTests

echo [2/4] Creating app-image with jpackage...
jpackage ^
  --type app-image ^
  --name "Mekong Farm" ^
  --app-version 1.0.0 ^
  --vendor "Mekong Farm Team" ^
  --description "Quản lý Nông sản Đồng bằng sông Cửu Long" ^
  --input target ^
  --main-jar mekong-farm-management-1.0.0.jar ^
  --main-class com.mekongfarm.Main ^
  --dest output ^
  --icon src/main/resources/images/icon.ico ^
  --java-options "-Xmx512m"

echo [3/4] Creating Windows installer (MSI)...
jpackage ^
  --type msi ^
  --name "Mekong Farm" ^
  --app-version 1.0.0 ^
  --vendor "Mekong Farm Team" ^
  --description "Quản lý Nông sản Đồng bằng sông Cửu Long" ^
  --input target ^
  --main-jar mekong-farm-management-1.0.0.jar ^
  --main-class com.mekongfarm.Main ^
  --dest installer ^
  --icon src/main/resources/images/icon.ico ^
  --win-dir-chooser ^
  --win-menu ^
  --win-shortcut ^
  --java-options "-Xmx512m"

echo [4/4] Done!
echo.
echo Installer created at: installer\Mekong Farm-1.0.0.msi
pause
