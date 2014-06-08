package AST.Visitor;

import AST.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
//import java.util.Iterator;
import java.util.Map;
import java.util.Set;


// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// HP 10/11

public class CodeGenVisitor implements Visitor {
    private SymbolTable symtable;
    private TypeSystem typesys;

    public static final int SIZE_INTEGER = 4;
    public static final int SIZE_BOOLEAN = 4;

    public static final String NAME_PROC_MAIN = "_asm_main";
    public static final String NAME_PROC_PRINT = "_put";

    private int retIntegerLiteral = 0;
    public void setTypeSystem(TypeSystem ts)
    {
        this.typesys = ts;
    }
    public void setSymbolTable(SymbolTable st)
    {
        this.symtable = st;
    }

    public CGHelper cgh = new CGHelper();

    public void createVTableNames() {
        for (Map.Entry<String, ClassInfo> classentry : symtable.st.entrySet()) {
            ClassInfo cval = classentry.getValue();
            cval.vtName = classentry.getKey() + "$$";
            cval.vtCtorName = classentry.getKey() + "$" + classentry.getKey();
            for (Map.Entry<String, MethodInfo> methodentry : cval.methods.entrySet()) {
                MethodInfo mval = methodentry.getValue();
                if (classentry.getValue().containsStaticMain && methodentry.getKey()=="main") {
                    mval.vtName = NAME_PROC_MAIN;
                }
                else {
                    mval.vtName = classentry.getKey() + "$" + methodentry.getKey();
                }
            }
        }
    }
    public void genVtableEntries()
    {
        cgh.gen("_DATA\tSEGMENT");
        cgh.gen("\r\n");

        for (Map.Entry<String, ClassInfo> classentry : symtable.st.entrySet()) {
            ClassInfo cval = classentry.getValue();
            cgh.gen(cval.vtName + ":");
            cgh.gen("\r\n");

            if (((ClassSymbolType)cval.type).baseClassType == null) {
                cgh.gen("\tDD 0");
                cgh.gen("\t");
                cgh.genCommentLine(" no base class");
            }
            else {
                ClassSymbolType base = (ClassSymbolType)((ClassSymbolType)cval.type).baseClassType;
                ClassInfo baseCI = symtable.lookup(base.name);
                cgh.gen("\tDD " + baseCI.vtName);
                cgh.gen("\t");
                cgh.genCommentLine(" base class " + base.name);
            }

/*
            //todo: should we support ctor's?

            // First add entry for default constructor
            cgh.gen("\tDD " + cval.vtCtorName);
            cgh.gen("\t");
            cgh.genCommentLine(" " + classentry.getKey() + " ctor");
*/
            for (Map.Entry<String, MethodInfo> methodentry : cval.methods.entrySet()) {
                MethodInfo mval = methodentry.getValue();
                cgh.gen("\tDD " + mval.vtName);
                cgh.gen("\t");
                cgh.genCommentLine(" " + classentry.getKey() +"::"+ methodentry.getKey());
            }
        }
        cgh.gen("\r\n");
        cgh.gen("_DATA\tENDS");
        cgh.gen("\r\n");
        cgh.gen("\r\n");
    }

    public void initGen()
    {
        cgh.initWriter(); // todo: remove me
        cgh.genAsmPreamble();
        genVtableEntries();
        cgh.gen("_TEXT	SEGMENT \r\n" +
                //"_argc$ = 8                       ; size = 4 \r\n" +
                //"_argv$ = 12                      ; size = 4 \r\n" +
                "\r\n");
    }

    // Display added for toy example language.  Not used in regular MiniJava
    public void visit(Display n) {

        n.e.accept(this);

    }

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {

        n.m.accept(this);
        for (int i = 0; i < n.cl.size(); i++) {

            n.cl.get(i).accept(this);
        }

        cgh.genAsmPostamble();
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        cgh.gen(NAME_PROC_MAIN + " PROC");
        cgh.gen("\t");
        cgh.genCommentLine("main");
        n.i1.accept(this);

        n.i2.accept(this);


        cgh.genCommentLine("Line " + n.s.line_number);

        cgh.genCalleeProlog(0);

        n.s.accept(this);

        cgh.genCalleeEpilog();

        cgh.gen("\r\n");
        cgh.gen(NAME_PROC_MAIN + " ENDP");
        cgh.gen("\t");
        cgh.genCommentLine("main");
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
        cgh.genCallerProlog(temp, NAME_PROC_PRINT);

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
