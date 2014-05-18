package AST.Visitor;

import java.util.HashMap;
//import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class ClassInfo
{
    SymbolType type;
    int ln;
    public String baseClass;

    public ClassInfo(int line_number)
    {
        type = new ClassSymbolType();
        ln = line_number;
        baseClass = null;
    }
}

public class SymbolTable
{
    HashMap<String, ClassInfo> st;

    public SymbolTable()
    {
        st = new HashMap<String, ClassInfo>();
    }

    public void enter(String s, ClassInfo ci)
    {
        if (st.containsKey(s)) {
            System.out.println("ERROR: duplicate class declaration of " + "\"" + s + "\"" + " at lines " + ci.ln + " and " + st.get(s).ln);
            throw new SemanticException();
        }
        st.put(s, ci);
    }

    public ClassInfo lookup(String s)
    {
        return st.get(s);
    }

    public void dump() {
        System.out.println("GLOBAL SYM TABLE:");
        for (Map.Entry<String, ClassInfo> entry : st.entrySet()) {
            ClassInfo val = entry.getValue();
            System.out.print(entry.getKey() + " (ln " + val.ln + ") :: " + val.toString());
            if (val.baseClass != null)
                System.out.println(" extends " + val.baseClass);
            else
                System.out.println();
            //val.dump();
        }
    }
}
