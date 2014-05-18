package AST.Visitor;

import java.util.List;
import java.util.ArrayList;

public class TypeSystem
{
    public static ArraySymbolType arrayt;
    public static BooleanSymbolType boolt;
    public static ClassSymbolType classt;
    public static IntSymbolType intt;
    public static LiteralSymbolType literalt;
    public static MethodSymbolType methodt;
    public static UnknownSymbolType unknownt;
    public static VoidSymbolType voidt;
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
