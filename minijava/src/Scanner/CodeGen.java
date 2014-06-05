package Scanner;

import AST.Program;

/**
 * Created by Muhammad on 6/4/2014.
 */
public class CodeGen {
    private final static int NO_ERROR = 0;

    public CodeGen()
    {}

    public int GenerateASMx86Code(Program p)
    {
        int error = ~NO_ERROR;

        return error;
    }
}

/*
======================================
======================================
Object layout in project 4
// see all
======================================
Initialization of variables?
Using uninitialized locals is undefined. It's probably a good idea to initialize them to 0, but it's not required. I will not test programs that use uninitialized locals.

        For object fields and arrays, Java specifies that they will be initialized to zero-like values. In our case, that means 0 for ints, false for booleans, and null for objects and arrays. For int arrays, they should all be zeroed out. I will be testing to make sure that fields and arrays are initialized to the correct default values.

        It's fine to change mjmalloc to return zeroed out memory.
======================================
*/