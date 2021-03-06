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
    private static SymbolTable st = null;
    private static TypeSystem ts = null;

    public static void main(String [] args) {
        int error = 1;

        try {
            scanner s = null;

/*
            for (int i = 0; i < args.length; i++) {
                System.out.println(args[i]);
            }
*/
            if (args.length == 2) {
                s = new scanner(new FileReader(args[1]));
            }
            else if (args.length == 1) {
                // create a scanner on the input file
                s = new scanner(System.in);
            }
            else {
                throw new Exception("Invalid ArgsLength: " + args.length);
            }

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
                error = SemanticChecks(program, true);
            }
            else if (args[0].startsWith("minijava")) {
                error = SemanticChecks(program, false); // we want to do semantic checks before code gen
                if (error == NO_ERROR)
                {
                    error = CodeGeneration(program, st, ts);
                }
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

    public static int SemanticChecks(Program program, boolean debug)
    {
        int error = ~NO_ERROR;

        SemanticAnalysis sa = new SemanticAnalysis();
        error = sa.pass1(program, debug);
        if (error == NO_ERROR) {
            error = sa.pass2(program, debug);
            if(error == NO_ERROR) {
                error = sa.pass3(program, debug);
            }
        }
        st = sa.symtable;
        ts = sa.typesys;

        return error;
    }

    public static int CodeGeneration(Program program, SymbolTable st, TypeSystem ts)
    {
        int error = ~NO_ERROR;

        CodeGen cg = new CodeGen(st, ts);

        cg.GenerateASMx86Code(program);

        return error;
    }
}

/*
BEFORE SUBMISSION:
todo:
. Remove env set= from build.cmd for [see email]
. Remove the file writer from Codegen
. todo: figure out why negative int literals cause syntax errors!!!
 */

