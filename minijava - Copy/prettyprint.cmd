@ECHO OFF

REM This should take a MiniJava program on stdin and print a syntactically equivalent version of the program to stdout. This behavior is implemented in PrettyPrintVisitor -- you just need to call it. It should exit with a 0 error 

java -classpath .\build\classes;.\lib\CUP.jar MyParser pp

