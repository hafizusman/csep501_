@ECHO OFF

REM ?minijavac.sh/minijavac.cmd: ?This should take a MiniJava program on stdin and write assembly code to stdout.
REM ?It should not write anything that is not assembly code to stdout -- this includes any output that Ant adds, or any diagnostic messages that you print.
REM ?It should exit with a zero exit code if and only if compilation is successful
REM ?When linked linked against your boot.c, your program should not produce any extraneous debugging output. It should only produce the output that the original program would produce if run in Java.


IF "%1"=="" GOTO Continue
java -classpath .\build\classes;.\lib\CUP.jar MyParser minijava %1
GOTO Exit

:Continue
java -classpath .\build\classes;.\lib\CUP.jar MyParser minijava
:Exit