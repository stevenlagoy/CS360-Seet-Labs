@echo off
for /f "tokens=5" %%a in ('netstat -aon ^| find ":3000" ^| find "LISTENING"') do taskkill /F /PID %%a
start /b npx json-server ./Data/db.json
ng serve
