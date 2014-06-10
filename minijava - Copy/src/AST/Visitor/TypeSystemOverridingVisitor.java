package AST.Visitor;

import AST.*;

// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// HP 10/11

/*
This visitor expects the caller to provide the symbol table as well as the type system objects

 */
public class TypeSystemOverridingVisitor implements Visitor {
    private SymbolTable symtable;
    private TypeSystem typesys;

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
        n.i1.accept(this);

        n.i2.accept(this);

        n.s.accept(this);
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {

        n.i.accept(this);

        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }

        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }


    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {

        n.i.accept(this);

        n.j.accept(this);
        ClassSymbolType basecst = (ClassSymbolType)symtable.lookup(n.i.s).type;
        ClassSymbolType derivedcst = (ClassSymbolType)symtable.lookup(n.j.s).type;

        for (int i = 0; i < basecst.methods.size(); i++) {
            MethodSymbolType bmst = (MethodSymbolType)basecst.methods.get(i);
            for (int j = 0; j < derivedcst.methods.size(); j++) {
                MethodSymbolType dmst = (MethodSymbolType) derivedcst.methods.get(i);
                if (bmst.name.equals(dmst.name)) {
                    if ( bmst.returnType != dmst.returnType &&
                            !isDerivedFrom((ClassSymbolType)dmst.returnType, (ClassSymbolType)bmst.returnType)) {
                        System.out.println("ERROR: " + n.j.line_number + ": overriding method should have same or more specific return type as base class method" );
                        throw new SemanticException();
                    }

                    if (bmst.paramListType.size() != dmst.paramListType.size()) {
                        System.out.println("ERROR: " + n.j.line_number + ": overriding method should have same number of params as base class method" );
                        throw new SemanticException();
                    }
                    for(int k = 0; k < bmst.paramListType.size(); k++) {
                        if (bmst.paramListType.get(k) != dmst.paramListType.get(k)) {
                            System.out.println("ERROR: " + n.j.line_number + ": overriding method should have same type of params as base class method" );
                            throw new SemanticException();
                        }
                    }
                }
            }
        }

        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
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
    }

    public void visit(BooleanType n) {
    }

    public void visit(IntegerType n) {
    }

    // String s;
    public void visit(IdentifierType n) {
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

        if (n.s !=null) {
            n.s.accept(this);
        }
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
