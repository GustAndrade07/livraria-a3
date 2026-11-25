@echo off
rem Compila e executa a Livraria A3 (Windows)
if exist out rmdir /s /q out
dir /s /b src\*.java > fontes.txt
javac -encoding UTF-8 -d out @fontes.txt
del fontes.txt
java -cp out loja.Main
