@echo off
title SCRIPT NÃO TESTADO CORRETAMENTE NO WINDOWS 
echo [LOG] Checando existência do PaperSpigot...

if exist PaperSpigot_1.8.8.jar (
  echo [OK] PaperSpigot_1.8.8.jar
) else (
  echo [ERR] PaperSpigot não encontrado no diretório atual
  exit
)

mvn install:install-file -Dfile=PaperSpigot_1.8.8.jar -DgroupId=com.destroystokyo.paper -DartifactId=paper-api -Dversion=1.8.8-R0.1-SNAPSHOT -Dpackaging=jar

pause >nul
