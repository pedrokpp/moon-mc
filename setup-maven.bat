@echo off
title Setup Maven - BATCH (https://github.com/pedrokpp/moon-mc/)
echo.
echo [LOG] Checando permissoes...

net session >nul 2>&1
if %errorLevel% == 0 (
  echo [ERRO] Nao execute o script como administrador
  pause >nul
  exit
) else (
     echo [OK] Executando sem permissoes de administrador
)
echo.
echo [LOG] Checando existencia do PaperSpigot...
if exist PaperSpigot_1.8.8.jar (
  echo [OK] PaperSpigot_1.8.8.jar
) else (
  echo [ERRO] PaperSpigot nao encontrado no diretorio atual
  echo [ERRO] Lembre-se de nomear o Paper como 'PaperSpigot_1.8.8.jar'
  pause >nul
  exit
)
echo.
mvn install:install-file -Dfile=PaperSpigot_1.8.8.jar -DgroupId=com.destroystokyo.paper -DartifactId=paper-api -Dversion=1.8.8-R0.1-SNAPSHOT -Dpackaging=jar
if %ERRORLEVEL% neq 0 goto erro

pause >nul

:erro
echo [ERRO] Maven não encontrado
set /p input=[?] Gostaria de abrir o site oficial do Maven para instalar? (s/n) 
if /I "%input%"=="s" goto yes
exit

:yes
start https://maven.apache.org/install.html
start https://maven.apache.org/download.cgi
