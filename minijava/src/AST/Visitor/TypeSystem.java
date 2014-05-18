package AST.Visitor;

import java.util.List;
import java.util.ArrayList;

public class TypeSystem
{
    ArraySymbolType arrayt;
    BooleanSymbolType boolt;
    ClassSymbolType classt;
    IntSymbolType intt;
    LiteralSymbolType literalt;
    MethodSymbolType methodt;
    UnknownSymbolType unknownt;
    VoidSymbolType voidt;
    public void init()
    {
        arrayt = new ArraySymbolType();
        boolt = new BooleanSymbolType();
        classt = new ClassSymbolType();
        intt = new IntSymbolType();
        literalt = new LiteralSymbolType();
        methodt = new MethodSymbolType();
        unknownt = new UnknownSymbolType();
        voidt = new VoidSymbolType();
    }

    public SymbolType lookup()
    {
        return unknownt;
    }
}
