package AST.Visitor;

import java.util.HashMap;
//import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/*
* SymTable
*   Class+
*       Field*
*       Method*
*           VarDecl*
*           Statement*
*               Expression*
*
GlobalSymbolTable
    ClassSymbolTable1
        Fields
        MethodSymbolTable1
            param
            lvars
            statements
                expressions
        MethodSymbolTable2
    ClassSymbolTable2
*/

class MethodSymbolTable {
}

class VariableDeclList {
}

class ClassSymbolTable {
    private HashMap<String, VariableDeclList> fields;
    private HashMap<String, MethodSymbolTable> methods;

    public ClassSymbolTable() {
        fields = new HashMap<String, VariableDeclList>();
        methods = new HashMap<String, MethodSymbolTable>();
    }

    public VariableDeclList lookupField(String id) {
        return fields.get(id);
    }

    public MethodSymbolTable lookupMethod(String id) {
        return methods.get(id);
    }

    public void enterField(String id, VariableDeclList vi)
    {
        if (fields.containsKey(id)) {
            System.out.println("CST: duplicate field declaration: " + id);
            throw new RuntimeException();
        }
        fields.put(id, vi);
    }

    public void enterMethod(String id, MethodSymbolTable mst)
    {
        if (methods.containsKey(id)) {
            System.out.println("CST: duplicate method declaration: " + id);
            throw new RuntimeException();
        }
        methods.put(id, mst);
    }

    public void open() {
    }

    public void close() {
    }

    public void dump() {
        System.out.println("\tCLASS SYM TABLE: <fields>");
        for (Map.Entry<String, VariableDeclList> entry : fields.entrySet()) {
            VariableDeclList val = entry.getValue();
            System.out.println("\t" + entry.getKey() + " :: " + val.toString());
        }

        System.out.println("\tCLASS SYM TABLE: <methods>");
        for (Map.Entry<String, MethodSymbolTable> entry : methods.entrySet()) {
            MethodSymbolTable val = entry.getValue();
            System.out.println("\t" + entry.getKey() + " :: " + val.toString());
            //val.dump();
        }
    }
}

public class GlobalSymbolTable {
    private final static int NO_ERROR = 0;

    private HashMap<String, ClassSymbolTable> symtable;

    public GlobalSymbolTable() {
        symtable = new HashMap<String, ClassSymbolTable>();
    }

    public ClassSymbolTable lookup(String id) {
        return symtable.get(id);
    }

    public void enter(String id, ClassSymbolTable si)
    {
        if (symtable.containsKey(id)) {
            System.out.println("GST: duplicate class identifier: " + id);
            throw new RuntimeException();
        }
        symtable.put(id, si);
    }

    public void open() {
    }

    public void close() {
    }

    public void dump() {
        System.out.println("GLOBAL SYM TABLE:");
        for (Map.Entry<String, ClassSymbolTable> symentry : symtable.entrySet()) {
            ClassSymbolTable cst = symentry.getValue();
            System.out.println(symentry.getKey() + " :: " + cst.toString());
            cst.dump();
        }
    }
}
