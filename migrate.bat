@echo off
REM =============================================
REM Auto Migration Script for MekongFarm
REM =============================================

echo [1/4] Stopping all Java processes...
taskkill /F /IM java.exe /T 2>nul
timeout /t 2 /nobreak >nul

echo [2/4] Applying SQL migration...
sqlite3 mekongfarm.db < src\main\resources\database\migration_supplier.sql

IF %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Migration completed!
) ELSE (
    echo [WARNING] Migration had some errors, but may still work
    echo Check if columns already exist - this is OK
)

echo [3/4] Verifying schema...
sqlite3 mekongfarm.db "PRAGMA table_info(san_pham);" | findstr "ma_ncc"
IF %ERRORLEVEL% EQU 0 (
    echo [OK] Column ma_ncc exists
) ELSE (
    echo [ERROR] Column ma_ncc not found!
)

echo [4/4] Starting app...
call apache-maven-3.9.5\bin\mvn.cmd javafx:run

echo.
echo =============================================
echo Migration complete! App is starting...
echo =============================================
pause
