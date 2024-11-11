@echo off
if not exist bin mkdir bin
if not exist dist mkdir dist
javac -d bin src/*.java
jar cfm dist/compilador.jar manifest.txt -C bin .
echo Compilacion completada. Use: java -jar dist/compilador.jar archivo.txt