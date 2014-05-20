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
    //private VarInfo currVariableI;
    //private FormalInfo currFormalI;
    //private FieldInfo currFieldI;
    //private LocalInfo currLocalI;

    public TypeSystem getTypeSystem()
    {
        return this.typesys;
    }
    public void setTypeSystem(TypeSystem ts)
    {
        this.typesys = ts;
    }
    public void setSymbolTable(SymbolTable st)
    {
        this.symtable = st;
    }

    private boolean isAssignableTo(SymbolType lh, SymbolType rh)
    {
        boolean assignable = false;
        if (lh == rh) {
            assignable = true;
        }
        else if ( lh instanceof ClassSymbolType && rh instanceof  ClassSymbolType) {
            if (isDerivedFrom((ClassSymbolType)lh, (ClassSymbolType)rh) == true) {
                assignable = true;
            }
        }

        return assignable;
    }

    private boolean isDerivedFrom(ClassSymbolType base, ClassSymbolType derived)
    {
        boolean isderived = false;
        boolean end = false;

        while(!end) {
            if (derived == null) {
                end = true;
            }
            else if (derived.baseClassType == base &&
                    derived.baseClassType != null &&
                    base != null) {
                isderived = true;
                end = true;
            }
            else {
                derived = (ClassSymbolType) derived.baseClassType;
            }
        }

        return isderived;
    }

    private SymbolType validateIdentifierType(int linenum)
    {
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            returnedType = typesys.lookup(returnedString);
            if (returnedType == null) {
                System.out.println("ERROR: " + linenum + ": unknown type \"" + returnedString + "\"" );
                throw new SemanticException();
            }
        }
        return returnedType;
    }

    private SymbolType validateIdentifierExp(int linenum)
    {
        VarInfo vi;
        if ((vi = currMI.lookupLocal(returnedString)) == null) {
            if ((vi = currMI.lookupFormal(returnedString)) == null) {
                if ((vi = currCI.lookupField(returnedString)) == null) {
                    System.out.println("ERROR: " + linenum + ": Undefined symbol \"" + returnedString + "\"");
                    throw new SemanticException();
                }
            }
        }
        return (returnedType = vi.type);
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
            //currVariableI = currFieldI = currCI.lookupField(n.vl.get(i).i.s);
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
            //currVariableI = currFieldI = currCI.lookupField(n.vl.get(i).i.s);
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

        n.i.accept(this);

        for ( int i = 0; i < n.fl.size(); i++ ) {
            n.fl.get(i).accept(this);
        }
        
        for ( int i = 0; i < n.vl.size(); i++ ) {
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
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.BOOL)) {
            System.out.println("ERROR: " + n.e.line_number + ": operand must be boolean type" );
            throw new SemanticException();
        }

        if (n.s1 !=null) {
            n.s1.accept(this);
        }

        if (n.s2 != null) {
            n.s2.accept(this);
        }
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        
        n.e.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.BOOL)) {
            System.out.println("ERROR: " + n.e.line_number + ": operand must be boolean type" );
            throw new SemanticException();
        }

        if (n.s !=null) {
            n.s.accept(this);
        }
    }

    // Exp e;
    public void visit(Print n) {
        
        n.e.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e.line_number + ": operand must be int type" );
            throw new SemanticException();
        }

    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        SymbolType lh, rh;
        VarInfo vi;

        if ((vi = currMI.lookupLocal(n.i.s)) == null) {
            if ((vi = currMI.lookupFormal(n.i.s)) == null) {
                if ((vi = currCI.lookupField(n.i.s)) == null) {
                    System.out.println("ERROR: " + n.line_number + ": Undefined symbol \"" + n.i.s + "\"");
                    throw new SemanticException();
                }
            }
        }
        lh = vi.type;

        n.i.accept(this);

        n.e.accept(this);
        rh = returnedType;
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            rh = validateIdentifierExp(n.e.line_number);
        }

        if (!isAssignableTo(lh, rh)) {
            System.out.println("ERROR: " + n.e.line_number + ": rhs not type compatible with lhs" );
            throw new SemanticException();
        }
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        SymbolType lh, rh;
        VarInfo vi;

        if ((vi = currMI.lookupLocal(n.i.s)) == null) {
            if ((vi = currMI.lookupFormal(n.i.s)) == null) {
                if ((vi = currCI.lookupField(n.i.s)) == null) {
                    System.out.println("ERROR: " + n.line_number + ": Undefined symbol \"" + n.i.s + "\"");
                    throw new SemanticException();
                }
            }
        }
        lh = vi.type;

        n.i.accept(this);
        
        n.e1.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e1.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e1.line_number + ": array index must be int type" );
            throw new SemanticException();
        }

        n.e2.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e1.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e1.line_number + ": array assignment rh must be int type" );
            throw new SemanticException();
        }
    }

    // Exp e1,e2;
    public void visit(And n) {
        
        n.e1.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e1.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.BOOL)) {
            System.out.println("ERROR: " + n.e1.line_number + ": And l-operand must be boolean type" );
            throw new SemanticException();
        }

        n.e2.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e2.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.BOOL)) {
            System.out.println("ERROR: " + n.e2.line_number + ": And r-operand must be boolean type" );
            throw new SemanticException();
        }

    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        
        n.e1.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e1.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e1.line_number + ": LessThan l-operand must be int type" );
            throw new SemanticException();
        }

        n.e2.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e2.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e2.line_number + ": LessThan r-operand must be int type" );
            throw new SemanticException();
        }

    }

    // Exp e1,e2;
    public void visit(Plus n) {
        n.e1.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e1.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e1.line_number + ": Plus l-operand must be int type" );
            throw new SemanticException();
        }

        n.e2.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e2.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e2.line_number + ": Plus r-operand must be int type" );
            throw new SemanticException();
        }

    }

    // Exp e1,e2;
    public void visit(Minus n) {
        
        n.e1.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e1.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e1.line_number + ": Minus l-operand must be int type" );
            throw new SemanticException();
        }

        n.e2.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e2.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e2.line_number + ": Minus r-operand must be int type" );
            throw new SemanticException();
        }

    }

    // Exp e1,e2;
    public void visit(Times n) {
        
        n.e1.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e1.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e1.line_number + ": Multiply l-operand must be int type" );
            throw new SemanticException();
        }

        n.e2.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e2.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e2.line_number + ": Multiply r-operand must be int type" );
            throw new SemanticException();
        }
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        n.e1.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e1.line_number);
        }

        n.e2.accept(this);
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e2.line_number + ": Integer expected for array lookup" );
            throw new SemanticException();
        }

        returnedType = typesys.lookup(TypeSystem.INT); //todo: remove this HACK that's in place to make assignment work with arr lookup
    }

    // Exp e;
    public void visit(ArrayLength n) {
        n.e.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.ARRAY)) {
            System.out.println("ERROR: " + n.e.line_number + ": array operand required to get length" );
            throw new SemanticException();
        }

        returnedType = typesys.lookup(TypeSystem.INT); //todo: remove this HACK that's in place to make assignment work with arr.length
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        boolean methodfound = false;
        boolean end = false;
        MethodSymbolType mst = null;

        n.e.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            returnedType = validateIdentifierExp(n.e.line_number);
        }
        if (!(returnedType instanceof ClassSymbolType)) {
            System.out.println("ERROR: " + n.e.line_number + ": methods can only be invoked on class objects" );
            throw new SemanticException();
        }

        ClassSymbolType t = (ClassSymbolType)returnedType;
        while(!end) {
            for (int i = 0; i < t.methods.size(); i++) {
                mst = (MethodSymbolType)t.methods.get(i);
                if (mst.name.equals(n.i.s)) {
                    methodfound = true;
                    end = true;
                    break;
                }
            }
            if (methodfound == false) {
                if (t.baseClassType == null) {
                    System.out.println("ERROR: " + n.e.line_number + ": method doesn't exist on object");
                    throw new SemanticException();
                }
                t = (ClassSymbolType)t.baseClassType;
            }
        }


        n.i.accept(this);
        
        if (n.el.size() != mst.paramListType.size()) {
            System.out.println("ERROR: " + n.el.line_number + ": method invalid num parameters" );
            throw new SemanticException();
        }

        for ( int i = 0; i < n.el.size(); i++ ) {
            n.el.get(i).accept(this);
            if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
                returnedType = validateIdentifierExp(n.e.line_number);
            }
            if (returnedType != mst.paramListType.get(i)) {
                System.out.println("ERROR: " + n.el.line_number + ": method invalid arg type: " + (i+1) );
                throw new SemanticException();
            }
        }

        returnedType = mst.returnType; // todo: remove this HACK by checkin for return type
    }

    // int i;
    public void visit(IntegerLiteral n) {
        returnedType = typesys.lookup(TypeSystem.INT);
    }

    public void visit(True n) {
        returnedType = typesys.lookup(TypeSystem.BOOL);
    }

    public void visit(False n) {
        returnedType = typesys.lookup(TypeSystem.BOOL);
    }

    // String s;
    public void visit(IdentifierExp n) {
        returnedType = typesys.lookup(TypeSystem.UNKNOWN); //means it's an identifier
        returnedString = n.s;
    }

    public void visit(This n) {
        returnedType = currCI.type;
    }

    // Exp e;
    public void visit(NewArray n) {
        n.e.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.INT)) {
            System.out.println("ERROR: " + n.e.line_number + ": length must be int type" );
            throw new SemanticException();
        }

        returnedType = typesys.lookup(TypeSystem.ARRAY); //todo: remove this HACK that's in place to make assignment work with new arrays
    }

    // Identifier i;
    public void visit(NewObject n) {
        returnedType = typesys.lookup(n.i.s);
        if (returnedType == null) {
            System.out.println("ERROR: " + n.i.line_number + ": unknown type \"" + n.i.s + "\"" );
            throw new SemanticException();
        }
    }

    // Exp e;
    public void visit(Not n) {
        n.e.accept(this);
        if (returnedType == typesys.lookup(TypeSystem.UNKNOWN)) {
            validateIdentifierExp(n.e.line_number);
        }
        if (returnedType != typesys.lookup(TypeSystem.BOOL)) {
            System.out.println("ERROR: " + n.e.line_number + ": operand must be boolean type" );
            throw new SemanticException();
        }
    }

    // String s;
    public void visit(Identifier n) {
    }
}
