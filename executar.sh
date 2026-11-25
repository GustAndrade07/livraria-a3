#!/bin/sh
# Compila e executa a Livraria A3 (Linux/macOS)
rm -rf out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out loja.Main
