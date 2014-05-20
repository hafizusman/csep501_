package AST.Visitor;

import AST.*;

// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// HP 10/11

/*
This visitor expects the caller to provide the symbol table as well as the type system objects

 */
public class TypeSystem2Visitor implements Visitor {
    private SymbolTable symtable;
    private TypeSystem typesys;

    private SymbolType returnedType;
    private String returnedString;

    private ClassInfo currCI;
    private MethodInfo currMI;
    private VarInfo currVariableI;
    private FormalInfo currFormalI;
    private FieldInfo currFieldI;
    private LocalInfo currLocalI;

    public TypeSystem getTypeSystem()
    {
        return this.typesys;
    }

    private void validateIdentifierType(int linenum)
    {
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            returnedType = typesys.lookup(returnedString);
            if (returnedType == null) {
                System.out.println("ERROR: " + linenum + ": unknown type \"" + returnedString + "\"" );
                throw new SemanticException();
            }
        }
    }

    public void setTypeSystem(TypeSystem ts)
    {
        this.typesys = ts;
    }
    public void setSymbolTable(SymbolTable st)
    {
        this.symtable = st;
    }

    // Display added for toy example language.  Not used in regular MiniJava
    public void visit(Display n) {
        
        n.e.accept(this);
        
    }

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        n.m.accept(this);
        for ( int i = 0; i < n.cl.size(); i++ ) {
            
            n.cl.get(i).accept(this);
        }
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        currCI = symtable.lookup(n.i1.s);

        n.i1.accept(this);

        currMI = currCI.lookupMethod("main");
        n.i2.accept(this);

        n.s.accept(this);
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        currCI = symtable.lookup(n.i.s);

        n.i.accept(this);
        
        for ( int i = 0; i < n.vl.size(); i++ ) {
            currVariableI = currFieldI = currCI.lookupField(n.vl.get(i).i.s);
            n.vl.get(i).accept(this);
        }

        for ( int i = 0; i < n.ml.size(); i++ ) {
            currMI = currCI.lookupMethod(n.ml.get(i).i.s);
            n.ml.get(i).accept(this);
        }
        
        
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        currCI = symtable.lookup(n.i.s);

        n.i.accept(this);
        
        n.j.accept(this);
        
        for ( int i = 0; i < n.vl.size(); i++ ) {
            currVariableI = currFieldI = currCI.lookupField(n.vl.get(i).i.s);
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
            currMI = currCI.lookupMethod(n.ml.get(i).i.s);
            n.ml.get(i).accept(this);
        }
        
        
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {
        n.t.accept(this);

        validateIdentifierType(n.i.line_number);

        if (currVariableI instanceof FieldInfo) {
            currCI.lookupField(n.i.s).type = returnedType;
        }
        else if (currVariableI instanceof LocalInfo) {
            currMI.lookupLocal(n.i.s).type = returnedType;
        }
        else {throw new RuntimeException("unknown variable info type");}

        n.i.accept(this);

    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {
        currMI = currCI.lookupMethod(n.i.s);

        n.t.accept(this);
        validateIdentifierType(n.t.line_number);

        n.i.accept(this);
        
        for ( int i = 0; i < n.fl.size(); i++ ) {
            currVariableI = currFormalI = currMI.lookupFormal(n.fl.get(i).i.s);
            n.fl.get(i).accept(this);
        }
        
        for ( int i = 0; i < n.vl.size(); i++ ) {
            currVariableI = currLocalI = currMI.lookupLocal(n.vl.get(i).i.s);
            n.vl.get(i).accept(this);
            
        }
        for ( int i = 0; i < n.sl.size(); i++ ) {
            
            n.sl.get(i).accept(this);
            if ( i < n.sl.size() ) {  }
        }
        
        n.e.accept(this);
        
        
    }

    // Type t;
    // Identifier i;
    public void visit(Formal n) {
        n.t.accept(this);

        validateIdentifierType(n.i.line_number);
        if (currVariableI instanceof FormalInfo) {
            currMI.lookupFormal(n.i.s).type = returnedType;
        }
        else {throw new RuntimeException("unknown variable info type for formal\n");}

        n.i.accept(this);
    }

    public void visit(IntArrayType n) {
        returnedType = typesys.lookup(TypeSystem.ARRAY);
    }

    public void visit(BooleanType n) {
        returnedType = typesys.lookup(TypeSystem.BOOL);
    }

    public void visit(IntegerType n) {
        returnedType = typesys.lookup(TypeSystem.INT);
    }

    // String s;
    public void visit(IdentifierType n) {
        returnedType = typesys.lookup(TypeSystem.UNKNOWN); //means it's an identifier
        returnedString = n.s;
    }

    // StatementList sl;
    public void visit(Block n) {
        
        for ( int i = 0; i < n.sl.size(); i++ ) {
            
            n.sl.get(i).accept(this);
            
        }
        
    }

    // Exp e;
    // Statement s1,s2;
    public void visit(If n) {
        
        n.e.accept(this);
        
        
        n.s1.accept(this);
        
        
        n.s2.accept(this);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        
        n.e.accept(this);
        
        n.s.accept(this);
    }

    // Exp e;
    public void visit(Print n) {
        
        n.e.accept(this);
        
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        n.i.accept(this);
        
        n.e.accept(this);
        
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        n.i.accept(this);
        
        n.e1.accept(this);
        
        n.e2.accept(this);
        
    }

    // Exp e1,e2;
    public void visit(And n) {
        
        n.e1.accept(this);
        
        n.e2.accept(this);
        
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        
        n.e1.accept(this);
        
        n.e2.accept(this);
        
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        
        n.e1.accept(this);
        
        n.e2.accept(this);
        
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        
        n.e1.accept(this);
        
        n.e2.accept(this);
        
    }

    // Exp e1,e2;
    public void visit(Times n) {
        
        n.e1.accept(this);
        
        n.e2.accept(this);
        
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        n.e1.accept(this);
        
        n.e2.accept(this);
        
    }

    // Exp e;
    public void visit(ArrayLength n) {
        n.e.accept(this);
        
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        n.e.accept(this);
        
        n.i.accept(this);
        
        for ( int i = 0; i < n.el.size(); i++ ) {
            n.el.get(i).accept(this);
            if ( i+1 < n.el.size() ) {  }
        }
        
    }

    // int i;
    public void visit(IntegerLiteral n) {
        
    }

    public void visit(True n) {
        
    }

    public void visit(False n) {
        
    }

    // String s;
    public void visit(IdentifierExp n) {
        
    }

    public void visit(This n) {
        
    }

    // Exp e;
    public void visit(NewArray n) {
        
        n.e.accept(this);
        
    }

    // Identifier i;
    public void visit(NewObject n) {
        
        
        
    }

    // Exp e;
    public void visit(Not n) {
        
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {
        
    }
}
