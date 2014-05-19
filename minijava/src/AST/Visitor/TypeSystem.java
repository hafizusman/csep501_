package AST.Visitor;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class TypeSystem
{
    public static final String BOOL = "boolean";
    public static final String INT = "int";
    public static final String UNKNOWN = "unknown";
    public static final String VOID = "void";

    private static int count = 0;
/*
    public ArraySymbolType arrayt;
    public BooleanSymbolType boolt;
    public ClassSymbolType classt;
    public IntSymbolType intt;
    public LiteralSymbolType literalt;
    public MethodSymbolType methodt;
    public UnknownSymbolType unknownt;
    public VoidSymbolType voidt;
    public IdentifierSymbolType idt;
*/
    private HashMap<String, SymbolType> st;

    public void init()
    {
        if(count >= 1) {
            System.out.println("ERROR: only one instance of TypeSystem allowed!");
            throw new SemanticException();
        }
        st = new HashMap<String, SymbolType>();
        st.put("boolean", new BooleanSymbolType());
        st.put("int", new IntSymbolType());
        st.put("unknown", new UnknownSymbolType());
        st.put("void", new VoidSymbolType());
        count++;
    }

    public SymbolType lookup(String s)
    {
        return st.get(s);
    }
    public void enter(String s, SymbolType t)
    {
        if (st.containsKey(s)) {
            System.out.println("ERROR: Cannot add type: " + s + ". Type already exists.");
            throw new SemanticException();
        }
        st.put(s, t);
    }

    public void dumpTypes()
    {
        System.out.println("TYPE SYSTEM:");
        for (Map.Entry<String, SymbolType> entry : st.entrySet()) {
            SymbolType val = entry.getValue();
            System.out.println(
                    entry.getKey() + " -> " +
                    "[" +
                    "type: " + val.toString() + ", " +
                    "]");
        }
        System.out.println();
    }
}
