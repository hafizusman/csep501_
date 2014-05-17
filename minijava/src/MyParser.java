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

        program.accept(new SymbolTableVisitor());

        return error;
    }
}



/*
=======
NOTES:
=======
HashMap should be particularly useful for symbol tables.
Use the List classes (ArrayList or LinkedList) for things like argument lists and field lists in classes.
    Don't reinvent any more wheels than necessary.

It can be useful to include a few auxiliary methods that perform common operations on types.
    Possibilities include a method that returns true if two types are the same, and
    a method that returns true if a value of one type is assignable to another. Also possibly useful:
    a method that tries to add an entry to a symbol table and reports an error if the name is already declared, and another
    that looks up an identifier and reports an error if it is not found (and maybe adds it to the symbol table with an "undefined" type, which can be used to suppress additional, extraneous error messages about the same identifier).

A useful tool is to augment the AST print visitor from the last assignment so it also prints type information where appropriate.
    This isn't required, but it might save some debugging time.

It's fine to halt after a single error message


=======
TEST:
=======
Ref: About project #3
Fields and methods in Java (and MiniJava) are in separate namespaces, so you can have a field and a method of the same name.
We allow overriding, but not overloading

Ref: Inheritance and member fields
Yes. Subclasses can define fields that shadow superclass fields. The semantics are a bit tricky to get right, but in this situation, any method defined in Base would access "int y" if it referred to "y," and any method defined in Derived would access "boolean y".
You should support field shadowing. For example, if a method in Derived had a line such as "y = 5;", your typechecker should reject the program.

Ref: VarDeclaration private
there's no way for a field to be referenced outside of the class in which it is defined, except subclasses

Ref: I don't get the Visitor pattern
// SEE AGAIN

Ref: Expressions in system.out.println
typechecker should reject print statements with boolean arguments.

Ref: Inheritance correctness question ..
// for overriding
The parameter types have to be identical. The return types just have to be assignable to the return type of the method that is overwritten.

Ref: Arrays and expected output from sample programs
You don't need to do array bounds checking.
Of course, you need to store the lengths
However, out of bounds indexes can be undefined behavior.
    Same with negative lengths at initialization.

Ref: Test case sharing thread
// try the test cases

Ref: Overriding Member Variables
Fields are shadowed, but methods are overridden. The semantics are entirely different for these two mechanisms.
an overriding method can return a subtype of the return type of the method that it overrides
    >Just to verify, we don't need another visitor to check that method overloading is not happening in the code for the projet, or do we?
    This should happen automatically by virtue of your code protecting against symbol redeclaration (will prevent overloading a method in the same class) and ensuring that overridden methods have appropriate signatures (will prevent overloading a method in an ancestor class).
You should check to make sure that no two methods in a class have the same name. This should be pretty easy to tack on without adding an extra visitor.

!!! The list we provided is meant more as a starting point than a complete list.

MORE CHECKS (ref slides)
Has a variable been declared before it is used?
Are types consistent in an expression?
In the assignment x=y, is y assignable to x?
Does a method call have right number and types of parameters?
In a selector p.q, is q a method or field of object p?
Is variable x guaranteed to be initialized before it is used?
Could p be null when p.q is executed?

 */