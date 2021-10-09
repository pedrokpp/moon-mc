#!/usr/bin/bash

echo [LOG] Checando existência do PaperSpigot...

if [[ -f "PaperSpigot_1.8.8.jar" ]]; then
  echo [OK] PaperSpigot_1.8.8.jar
else
  echo [ERR] PaperSpigot não encontrado no diretório atual
  exit
fi
        
mvn install:install-file -Dfile=PaperSpigot_1.8.8.jar -DgroupId=com.destroystokyo.paper -DartifactId=paper-api -Dversion=1.8.8-R0.1-SNAPSHOT -Dpackaging=jar 
