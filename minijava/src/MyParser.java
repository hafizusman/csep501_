import Scanner.*;
import Parser.*;
import AST.*;
import AST.Visitor.*;
import java_cup.runtime.Symbol;

import java.io.FileReader;
import java.util.*;

public class MyParser {
    public void mYfunc()
    {
        System.out.println("QWERRTY");
    }

    public static void main(String [] args) {
        try {
            // create a scanner on the input file
            //scanner s = new scanner(System.in);
            scanner s = new scanner(new FileReader("C:\\eclipse\\csep501_\\minijava\\MyExample.java"));
            parser p = new parser(s);
            Symbol root;
	    // replace p.parse() with p.debug_parse() in next line to see trace of
	    // parser shift/reduce actions during parse
            if (false)
            {
                root = p.parse();
                //root = p.debug_parse();
                ArrayList<Statement> program = (ArrayList<Statement>)root.value;
                for (Statement statement: program) {
                    statement.accept(new PrettyPrintVisitor());
                    System.out.print("\n");
                }
            }
            else
            {
                root = p.parse();
                //root = p.debug_parse();
                if (true) {
                    ClassDecl program = (ClassDecl) root.value;
                    program.accept(new PrettyPrintVisitor());
                }
                else {
                    MethodDeclList program = (MethodDeclList) root.value;
                    for (int i = 0; i < program.size(); i++) {
                        MethodDecl d = program.get(i);
                        d.accept(new PrettyPrintVisitor());
                        System.out.print("\n");
                    }
                }
            }
            System.out.print("\nParsing completed");
        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " + 
                               e.toString());
            // print out a stack dump
            e.printStackTrace();
        }
    }
}