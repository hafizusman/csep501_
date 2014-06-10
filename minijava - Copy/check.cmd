@ECHO OFF

REM This should take a MiniJava program on stdin and exit with a 0 error code if and only if the program passed all semantic checks (and passed scanning and parsing). If it fails, it should print the error message(s) to stderr.

java -classpath .\build\classes;.\lib\CUP.jar MyParser check
