package AST.Visitor;

import java.util.HashMap;
//import java.util.Iterator;
import java.util.Map;
import java.util.Set;


class VarInfo
{
    public SymbolType type;
    public int ln;
    public int seqnum;
    public int ebpoffset; //set during code gen
    public VarInfo(int line_number)
    {
        type = new UnknownSymbolType();
        seqnum = -1;
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
    public SymbolType type;
    public int ln;
    int ordinal;
    int formalsSeqNum;
    int localsSeqNum;
    String vtName;
    HashMap <String, FormalInfo> formals;
    HashMap <String, LocalInfo> locals;

    public MethodInfo(int line_number)
    {
        type = new UnknownSymbolType();
        ln = line_number;
        formalsSeqNum = 1;
        localsSeqNum = 1;
        formals = new HashMap<String, FormalInfo>();
        locals = new HashMap<String, LocalInfo>();
        ordinal = -1;
        vtName = null; // will be filled in during code gen
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

    public FormalInfo lookupFormal(String s)
    {
        return formals.get(s);
    }
    public LocalInfo lookupLocal(String s)
    {
        return locals.get(s);
    }

    public void dump()
    {
        if (formals.size() > 0) {
            System.out.println("    METHOD INFO: <FORMALS>");
            for (Map.Entry<String, FormalInfo> entry : formals.entrySet()) {
                FormalInfo val = entry.getValue();
                System.out.println(
                        "      " +
                                entry.getKey() + " -> " +
                                "[" +
                                "type: " + val.type.toString() + ", " +
                                "line: " + val.ln + ", " +
                                "ord: " + val.seqnum +
                                "]"
                );
            }
        }
        if (locals.size() > 0) {
            System.out.println("    METHOD INFO: <LOCALS>");
            for (Map.Entry<String, LocalInfo> entry : locals.entrySet()) {
                LocalInfo val = entry.getValue();
                System.out.println(
                        "      " +
                                entry.getKey() + " -> " +
                                "[" +
                                "type: " + val.type.toString() + ", " +
                                "line: " + val.ln + ", " +
                                "ord: " + val.seqnum +
                                "]"
                );
            }
        }

    }
}

class ClassInfo
{
    public SymbolType type;
    public int ln;
    int fieldOrdinal;
    int methodOrdinal;
    String vtName;
    String vtCtorName;
    boolean containsStaticMain;
    HashMap <String, FieldInfo> fields;
    HashMap <String, MethodInfo> methods;

    public String baseClass;
    public boolean getIsStaticMainClass()
    {return containsStaticMain;}

    public ClassInfo(int line_number)
    {
        type = new UnknownSymbolType();
        ln = line_number;
        baseClass = null; //todo: we don't need this! it'll be stored in the typesystem
        fields = new HashMap<String, FieldInfo>();
        methods = new HashMap<String, MethodInfo>();
        methodOrdinal = 1;
        fieldOrdinal = 1;
        vtName = null; // will be filled in during code gen
        vtCtorName = null; // will be filled in during code gen
        containsStaticMain = false;
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

    public FieldInfo lookupField(String s)
    {
        return fields.get(s);
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
                System.out.println(
                        "    " +
                                entry.getKey() + " -> " +
                                "[" +
                                "type: " + val.type.toString() + ", " +
                                "line: " + val.ln + ", " +
                                "ord: " + val.seqnum +
                                "]"
                );
                //val.dump();
            }
        }

        if (methods.size() > 0) {
            System.out.println("  CLASS SYM TABLE: <METHOD>");
            for (Map.Entry<String, MethodInfo> entry : methods.entrySet()) {
                MethodInfo val = entry.getValue();
                System.out.println(
                        "    " +
                                entry.getKey() + " -> " +
                                "[" +
                                "t: " + val.type.toString() + ", " +
                                "l: " + val.ln + ", " +
                                "o: " + val.ordinal + ", " +
                                //"r: " + val.returnType.toString() +
                                "]"
                );

                val.dump();
            }
        }
    }

}

public class SymbolTable
{
    public HashMap<String, ClassInfo> st;

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
            System.out.print(   entry.getKey() + " -> " +
                                "[" +
                                "type: " + val.type.toString() + ", " +
                                "line: " + val.ln +
                                "]");
            if (val.baseClass != null)
                System.out.println(" extends " + val.baseClass);
            else
                System.out.println();
            val.dump();
        }
    }
}
