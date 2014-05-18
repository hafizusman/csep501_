package AST.Visitor;

import java.util.HashMap;
//import java.util.Iterator;
import java.util.Map;
import java.util.Set;


class LocalInfo
{
    SymbolType type;
    int ln;
    public int seqnum;
    public LocalInfo(int line_number)
    {
        seqnum = -1;
        type = new UnknownSymbolType(); //todo: figure out the actual type, i.e. is it a base type or a compound type
        ln = line_number;
    }
}

class FieldInfo
{
    SymbolType type;
    int ln;
    public int seqnum;
    public FieldInfo(int line_number)
    {
        seqnum = -1;
        type = new UnknownSymbolType(); //todo: figure out the actual type, i.e. is it a base type or a compound type
        ln = line_number;
    }
}

class MethodInfo
{
    SymbolType type;
    int ln;
    public SymbolType returnType;
    int ordinal;
    int localsSeqNum;
    HashMap <String, LocalInfo> locals;

    public MethodInfo(int line_number)
    {
        type = new MethodSymbolType();
        ln = line_number;
        localsSeqNum = 1;
        locals = new HashMap<String, LocalInfo>();
        ordinal = -1;
    }

    public void enterLocal(String s, LocalInfo li)
    {
        if (locals.containsKey(s)) {
            System.out.println("ERROR: duplicate local declaration of " + "\"" + s + "\"" + " at lines " + li.ln + " and " + locals.get(s).ln);
            throw new SemanticException();
        }
        li.seqnum = localsSeqNum++;
        locals.put(s, li);
    }

    public void dump()
    {
        //System.out.println("    METHOD INFO:"); //todo: open
        //System.out.println("      return type: " + returnType.toString());
        if (locals.size() > 0) {
            System.out.println("    METHOD INFO: <LOCALS>");
            for (Map.Entry<String, LocalInfo> entry : locals.entrySet()) {
                LocalInfo val = entry.getValue();
                System.out.println("    " + entry.getKey() + " (ln " + val.ln + ") :: " + "seqnum: " + val.seqnum + " " + val.toString());
                //val.dump();
            }
        }

    }
}

class ClassInfo
{
    SymbolType type;
    int ln;
    int fieldOrdinal;
    int methodOrdinal;
    HashMap <String, FieldInfo> fields;
    HashMap <String, MethodInfo> methods;

    public String baseClass;

    public ClassInfo(int line_number)
    {
        type = new ClassSymbolType();
        ln = line_number;
        baseClass = null;
        fields = new HashMap<String, FieldInfo>();
        methods = new HashMap<String, MethodInfo>();
        methodOrdinal = 1;
        fieldOrdinal = 1;
    }

    public void enterMethod(String s, MethodInfo mi)
    {
        if (methods.containsKey(s)) {
            System.out.println("ERROR: duplicate method declaration of " + "\"" + s + "\"" + " at lines " + mi.ln + " and " + methods.get(s).ln);
            throw new SemanticException();
        }
        mi.ordinal = methodOrdinal++;
        methods.put(s, mi);
    }

    public void enterField(String s, FieldInfo fi)
    {
        if (fields.containsKey(s)) {
            System.out.println("ERROR: duplicate field declaration of " + "\"" + s + "\"" + " at lines " + fi.ln + " and " + fields.get(s).ln);
            throw new SemanticException();
        }
        fi.seqnum = fieldOrdinal++;
        fields.put(s, fi);
    }

    public MethodInfo lookupMethod(String s)
    {
        return methods.get(s);
    }

    public void dump() {
        if (fields.size() > 0) {
            System.out.println("  CLASS SYM TABLE: <FIELD>");
            for (Map.Entry<String, FieldInfo> entry : fields.entrySet()) {
                FieldInfo val = entry.getValue();
                System.out.println("  " + entry.getKey() + " (ln " + val.ln + ") :: " + "ord: " + val.seqnum + " " + val.toString());

                //val.dump();
            }
        }

        if (methods.size() > 0) {
            System.out.println("  CLASS SYM TABLE: <METHOD>");
            for (Map.Entry<String, MethodInfo> entry : methods.entrySet()) {
                MethodInfo val = entry.getValue();
                System.out.println("  " + entry.getKey() + " (ln " + val.ln + ") :: " + "ord: " + val.ordinal + " " + val.toString());
                val.dump();
            }
        }
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
            val.dump();
        }
    }
}
