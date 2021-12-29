@echo off
title SCRIPT NÃO TESTADO CORRETAMENTE NO WINDOWS 
echo [LOG] Checando existencia do PaperSpigot...

if exist PaperSpigot_1.8.8.jar (
  echo [OK] PaperSpigot_1.8.8.jar
) else (
  echo [ERR] PaperSpigot nao encontrado no diretorio atual
  echo [ERR] Lembre-se de nomear o Paper como 'PaperSpigot_1.8.8.jar'
  pause >nul
  exit
)

mvn install:install-file -Dfile=PaperSpigot_1.8.8.jar -DgroupId=com.destroystokyo.paper -DartifactId=paper-api -Dversion=1.8.8-R0.1-SNAPSHOT -Dpackaging=jar
if %ERRORLEVEL% neq 0 goto erro

pause >nul

:erro
echo [ERR] Maven não encontrado
set /p input=[?] Gostaria de abrir o site oficial do Maven para instalar? (s/n) 
if /I "%input%"=="s" goto yes
exit

:yes
start https://maven.apache.org/install.html
start https://maven.apache.org/download.cgi
