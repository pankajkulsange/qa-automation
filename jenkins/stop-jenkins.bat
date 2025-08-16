@echo off
echo Stopping Jenkins...
taskkill /F /IM java.exe /FI "WINDOWTITLE eq jenkins*" 2>nul
echo Jenkins stopped.
