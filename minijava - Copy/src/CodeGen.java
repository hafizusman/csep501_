import AST.Program;
import AST.Visitor.CodeGenVisitor;
import AST.Visitor.SemanticException;
import AST.Visitor.SymbolTable;
import AST.Visitor.TypeSystem;



import java.util.Map;

/**
 * Created by Muhammad on 6/4/2014.
 */
public class CodeGen {
    private final static int NO_ERROR = 0;
    private SymbolTable symtable = null;
    private TypeSystem typesys = null;

    public CodeGen(SymbolTable st, TypeSystem ts)
    {
        this.symtable = st;
        this.typesys = ts;
    }

    public int GenerateASMx86Code(Program p)
    {
        int err = NO_ERROR;

        CodeGenVisitor viscg = new CodeGenVisitor();

        try
        {
            viscg.setTypeSystem(typesys);
            viscg.setSymbolTable(symtable);
            viscg.createVTableNames();

            viscg.initGen();
            p.accept(viscg);
        }
        catch (SemanticException e)
        {
            err = ~NO_ERROR;
        }

        return err;
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
vtable syntax (cannot compile with vtable)
// see examples
-----------------------
Below is a snip that works for me (Windows/x86 on x64 machine):
Hope this helps.

_DATA SEGMENT

; VTable for B
$B   dd 0
       dd _B_Test
	   dd _B_Test2
; VTable for D
$D   dd $B
	   dd _D_Test
	   dd _B_Test2
	   dd _D_Test3

_DATA ENDS
-----------------------
http://en.wikibooks.org/wiki/X86_Disassembly
======================================
Debugging a Hang Situation
// see other tips as well
Tips:
1. Insert a hard breakpoint in your assembly code where you want to debug. I used 'INT 3' instruction. This will cause the program to break into the debugger at the point when INT 3 runs (if you are running the program under a debugger)
2. You can use WinDBG as the debugger. Simply run the program under Windbg. Example: WinDbg.exe prog.exe
3. You can then step through the assembly code instruction by instruction using the F10 command

Some other tips that helped us with debugging:
1.Have unit tests for each small construct (we found bugs much faster in simpler apps)
2.Enable ML.exe PDB generation - this will allow you to load the commented *.asm file instead of going through the unassembled one.
3.Enable AppVerifier with basic checks - sometimes it helps by crashing the app faster instead of allowing corruption to propagate.
4.Enable GFlags heap checks
5.Try out the assembler code in an interractive debugger (we used OllyDbg and patched the exe in memory, stepping through the instructions to see the actual behavior and how the registers are modified).
6.In WinDBG we used the memory inspection window a lot (@esp, @ebp and pasting in various memory addresses to inspect the objects and their vtables)
======================================
From Lecture slides: see M-x86-64-project.pptx
"Generating .asm Code"
Suggestion: isolate the actual compiler output operations in a handful of routines

 write string to .asm output file void gen(String s) { … }

 write 'op src, dst' to .asm output file void genbin(String op, String src, String dst) { … }

 write label L to .asm output file as 'Ls:' void genLabel(String s) { … }

  A handful of these methods should do it
-----------------------
"External Names"
In Windows and OS X, an external symbol xyzzy is written in asm code as _xyzzy (leading underscore)

======================================

*/