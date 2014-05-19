import Scanner.*;
import Parser.*;
import AST.*;
import AST.Visitor.*;
import java_cup.runtime.Symbol;

import java.io.FileReader;
import java.util.*;

public class MyParser
{
    private final static int NO_ERROR = 0;

    public static void main(String [] args) {
        int error = 1;
        try {
            // create a scanner on the input file
            //scanner s = new scanner(System.in);
            scanner s = new scanner(new FileReader("C:\\eclipse\\csep501_\\minijava\\MyExample.java"));
            parser p = new parser(s);
            Symbol root;
            // replace p.parse() with p.debug_parse() in next line to see trace of
            // parser shift/reduce actions during parse
            root = p.parse();
            //root = p.debug_parse();

            Program program = (Program) root.value;

            if (args[0].startsWith("ast")) {
                program.accept(new ASTVisitor());
            }
            else if (args[0].startsWith("pp")) {
                program.accept(new PrettyPrintVisitor());
            }
            else if (args[0].startsWith("check")) {
                error = SemanticChecks(program);
            }
        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " + 
                               e.toString());
            // print out a stack dump
            e.printStackTrace();
        }

        System.exit(error);
    }

    public static int SemanticChecks(Program program)
    {
        int error = ~NO_ERROR;

        SemanticAnalysis sa = new SemanticAnalysis();
        error = sa.pass1(program);
        if (error == NO_ERROR) {
            error = sa.pass2(program);
            if(error == NO_ERROR) {
                error = sa.pass3(program);
            }
        }
        return error;
    }
}

/*
BEFORE SUBMISSION:
. Revert 'scanner s = new scanner(' in main
. Remove env set= from build.cmd for [see email]
 */