@echo off
setlocal

if not exist ".env" (
  echo No backend\.env found.
  echo Starting with default H2 database.
  echo To use Supabase, copy .env.example to .env and fill DB_PASSWORD.
  echo.
) else (
  for /f "usebackq tokens=1,* delims==" %%A in (".env") do (
    if not "%%A"=="" if not "%%A:~0,1%"=="#" set "%%A=%%B"
  )
)

mvn spring-boot:run
