package AST.Visitor;

import java.util.HashMap;
//import java.util.Iterator;
import java.util.Map;
import java.util.Set;


class VarInfo
{
    SymbolType type;
    public int ln;
    public int seqnum;
    public VarInfo(int line_number)
    {
        seqnum = -1;
        type = new UnknownSymbolType(); //todo: figure out the actual type, i.e. is it a base type or a compound type
        ln = line_number;
    }
    public VarInfo()
    {
        this(-1);
    }
}

class LocalInfo extends VarInfo
{
    public LocalInfo(int line_number)
    {
        super(line_number);
    }
    public LocalInfo()
    {
        this(-1);
    }
}

class FormalInfo extends VarInfo
{
    public FormalInfo(int line_number)
    {
        super(line_number);
    }
    public FormalInfo()
    {
        this(-1);
    }
}

class FieldInfo extends VarInfo
{
    public FieldInfo(int line_number)
    {
        super(line_number);
    }
    public FieldInfo()
    {
        this(-1);
    }
}

class MethodInfo
{
    SymbolType type;
    public int ln;
    public SymbolType returnType;
    int ordinal;
    int formalsSeqNum;
    int localsSeqNum;
    HashMap <String, FormalInfo> formals;
    HashMap <String, LocalInfo> locals;

    public MethodInfo(int line_number)
    {
        type = new MethodSymbolType();
        ln = line_number;
        formalsSeqNum = 1;
        localsSeqNum = 1;
        formals = new HashMap<String, FormalInfo>();
        locals = new HashMap<String, LocalInfo>();
        ordinal = -1;
    }
    public MethodInfo()
    {
        this(-1);
    }

    public void enterFormal(String s, FormalInfo fi)
    {
        if (formals.containsKey(s)) {
            System.out.println("ERROR: duplicate formal/formal declaration of " + "\"" + s + "\"" + " at lines " + fi.ln + " and " + formals.get(s).ln);
            throw new SemanticException();
        }
        if (locals.containsKey(s)) {
            System.out.println("ERROR: duplicate formal/local declaration of " + "\"" + s + "\"" + " at lines " + fi.ln + " and " + locals.get(s).ln);
            throw new SemanticException();
        }
        fi.seqnum = formalsSeqNum++;
        formals.put(s, fi);
    }

    public void enterLocal(String s, LocalInfo li)
    {
        if (formals.containsKey(s)) {
            System.out.println("ERROR: duplicate local/formal declaration of " + "\"" + s + "\"" + " at lines " + li.ln + " and " + formals.get(s).ln);
            throw new SemanticException();
        }
        if (locals.containsKey(s)) {
            System.out.println("ERROR: duplicate local/local declaration of " + "\"" + s + "\"" + " at lines " + li.ln + " and " + locals.get(s).ln);
            throw new SemanticException();
        }
        li.seqnum = localsSeqNum++;
        locals.put(s, li);
    }


    public void dump()
    {
        //System.out.println("    METHOD INFO:"); //todo: open
        //System.out.println("      return type: " + returnType.toString());
        if (formals.size() > 0) {
            System.out.println("    METHOD INFO: <FORMALS>");
            for (Map.Entry<String, FormalInfo> entry : formals.entrySet()) {
                FormalInfo val = entry.getValue();
                System.out.println("    " + entry.getKey() + " (ln " + val.ln + ") :: " + "seqnum: " + val.seqnum + " " + val.toString());
                //val.dump();
            }
        }
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
    public int ln;
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
    public ClassInfo()
    {
        this(-1);
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
