import AST.*;
import AST.Visitor.*;

public class SemanticAnalysis
{
    private final static int NO_ERROR = 0;

    public int pass1(Program p)
    {
        int err = ~NO_ERROR;
        p.accept(new SymbolTableVisitor());

        return err;
    }
}