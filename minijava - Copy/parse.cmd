@ECHO OFF

REM This should take a MiniJava program on stdin and print the indented tree version of the AST, described above, to stdout. It should exit with a 0 error code if and only if the parse was successful.

java -classpath .\build\classes;.\lib\CUP.jar MyParser ast
