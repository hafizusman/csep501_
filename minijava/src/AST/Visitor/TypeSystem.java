package AST.Visitor;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class TypeSystem
{
    public static final String BOOL = "boolean";
    public static final String INT = "int";
    public static final String ARRAY = "int[]";
    public static final String UNKNOWN = "unknown";
    public static final String VOID = "void";

    private static int count = 0;
    private HashMap<String, SymbolType> st;

    public void init()
    {
        if(count >= 1) {
            System.out.println("ERROR: only one instance of TypeSystem allowed!");
            throw new SemanticException();
        }
        st = new HashMap<String, SymbolType>();
        st.put(BOOL, new BooleanSymbolType());
        st.put(INT, new IntSymbolType());
        st.put(ARRAY, new ArraySymbolType());
        st.put(UNKNOWN, new UnknownSymbolType());
        st.put(VOID, new VoidSymbolType());
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
