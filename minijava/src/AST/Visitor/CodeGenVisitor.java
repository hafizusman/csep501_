package AST.Visitor;

import AST.*;

// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// HP 10/11

public class CodeGenVisitor implements Visitor {
    public static final int SIZE_INTEGER = 4;
    public static final int SIZE_BOOLEAN = 4;

    public static final String NAME_PRINTFUNC = "_put";
    private int retIntegerLiteral = 0;

    public CGHelper cgh = new CGHelper();

    // Display added for toy example language.  Not used in regular MiniJava
    public void visit(Display n) {

        n.e.accept(this);

    }

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        cgh.initWriter(); // todo: remove me
        cgh.genAsmPreamble();

        n.m.accept(this);
        for (int i = 0; i < n.cl.size(); i++) {

            n.cl.get(i).accept(this);
        }

        cgh.genAsmPostamble();
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        cgh.gen("_asm_main PROC");
        cgh.gen("\n");
        n.i1.accept(this);

        n.i2.accept(this);


        cgh.genCommentLine("Line " + n.s.line_number);

        cgh.genCalleeProlog(0);

        n.s.accept(this);

        cgh.genCalleeEpilog();

        cgh.gen("\n");
        cgh.gen("_asm_main ENDP");
        cgh.gen("\n");
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {

        n.i.accept(this);

        for (int i = 0; i < n.vl.size(); i++) {

            n.vl.get(i).accept(this);
            if (i + 1 < n.vl.size()) {
            }
        }
        for (int i = 0; i < n.ml.size(); i++) {

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

        for (int i = 0; i < n.vl.size(); i++) {

            n.vl.get(i).accept(this);
            if (i + 1 < n.vl.size()) {
            }
        }
        for (int i = 0; i < n.ml.size(); i++) {

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

        for (int i = 0; i < n.fl.size(); i++) {
            n.fl.get(i).accept(this);
            if (i + 1 < n.fl.size()) {
            }
        }

        for (int i = 0; i < n.vl.size(); i++) {

            n.vl.get(i).accept(this);

        }
        for (int i = 0; i < n.sl.size(); i++) {

            n.sl.get(i).accept(this);
            if (i < n.sl.size()) {
            }
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

        for (int i = 0; i < n.sl.size(); i++) {

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
        int [] temp = new int [1];

        n.e.accept(this);
        temp[0] = retIntegerLiteral;
        cgh.genCallerProlog(temp, NAME_PRINTFUNC);

        cgh.genCallerEpilog(SIZE_INTEGER); // we can only push one integer argument for the print method!
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
        int temp;

        n.e1.accept(this);
        temp = retIntegerLiteral;

        n.e2.accept(this);
        temp += retIntegerLiteral;

        retIntegerLiteral = temp;
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        int temp = 0;

        n.e1.accept(this);
        temp = retIntegerLiteral;

        n.e2.accept(this);
        temp -= retIntegerLiteral;
        retIntegerLiteral = temp;

    }

    // Exp e1,e2;
    public void visit(Times n) {
        int temp = 0;

        n.e1.accept(this);
        temp = retIntegerLiteral;

        n.e2.accept(this);
        temp *= retIntegerLiteral;
        retIntegerLiteral = temp;

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

        for (int i = 0; i < n.el.size(); i++) {
            n.el.get(i).accept(this);
            if (i + 1 < n.el.size()) {
            }
        }

    }

    // int i;
    public void visit(IntegerLiteral n) {
        retIntegerLiteral = n.i;
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
