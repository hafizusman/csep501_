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
    private ClassInfo currCI;
    private MethodInfo currMI;
    private String retClassName;

    public static final int SIZE_INTEGER = 4;
    public static final int SIZE_BOOLEAN = 4;
    public static final int SIZE_ARRAY_REF = 4;
    public static final int SIZE_OBJECT_REF = 4;

    public static final String NAME_PROC_MAIN = "_asm_main";
    public static final String NAME_PROC_MALLOC = "_mjmalloc";
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

    private void setupSymbolTableSizes()
    {
        // first setup the sizes of the fields within a class
        for (Map.Entry<String, ClassInfo> classentry : symtable.st.entrySet()) {
            ClassInfo cval = classentry.getValue();
            cval.type.bytes = 0;
            for (Map.Entry<String, FieldInfo> fieldentry : cval.fields.entrySet()) {
                cval.type.bytes += 4; //todo: make more dynamic but for now everything is 4 bytes (int, int[], bool and obj ref)
            }
        }
    }

    private int getObjectSize(ClassInfo ci)
    {
        int retSize = 4; //for the 'this' pointer

        return retSize;
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
                //
                // todo: need to make sure that vtable entries for derived class are in the same order as base class
                //
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
            int vtableoffset = 1; // first 4 bytes are for base class vtable hence starting from 1
            for (Map.Entry<String, MethodInfo> methodentry : cval.methods.entrySet()) {
                MethodInfo mval = methodentry.getValue();
                cgh.gen("\tDD " + mval.vtName);
                cgh.gen("\t");
                cgh.genCommentLine(" " + classentry.getKey() +"::"+ methodentry.getKey());
                mval.ordinal = vtableoffset * 4;
                vtableoffset++;
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

        setupSymbolTableSizes(); //todo: move to some semantics check visitor

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

        currCI = symtable.lookup(n.i1.s);

        n.i2.accept(this);

        cgh.genCommentLine("Line " + n.s.line_number);

        cgh.genCalleeProlog(0);

        currMI = currCI.lookupMethod("main");
        n.s.accept(this);

        cgh.genCalleeEpilog();

        cgh.gen(NAME_PROC_MAIN + " ENDP");
        cgh.gen("\t");
        cgh.genCommentLine("main");
        cgh.gen("_TEXT	ENDS");
        cgh.gen("\r\n");
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {

        n.i.accept(this);

        currCI = symtable.lookup(n.i.s);

        for (int i = 0; i < n.vl.size(); i++) {

            n.vl.get(i).accept(this);
            if (i + 1 < n.vl.size()) {
            }
        }
        for (int i = 0; i < n.ml.size(); i++) {
            currMI = currCI.lookupMethod(n.ml.get(i).i.s);
            n.ml.get(i).accept(this);
        }


    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {

        n.i.accept(this);

        currCI = symtable.lookup(n.i.s);

        n.j.accept(this);

        for (int i = 0; i < n.vl.size(); i++) {

            n.vl.get(i).accept(this);
            if (i + 1 < n.vl.size()) {
            }
        }
        for (int i = 0; i < n.ml.size(); i++) {
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
        String vtableMethodName = currMI.vtName;

/*
        _TEXT	SEGMENT
        B$my2:
        push	ebp
        mov	ebp, esp
        push	789
        call	_put
        add	esp, 4
        mov	esp, ebp
        pop	ebp
        ret
        _TEXT	ENDS
*/

        cgh.gen("_TEXT\tSEGMENT"); cgh.gen("\r\n");
        cgh.gen(vtableMethodName + ":"); cgh.gen("\r\n");

        cgh.genCommentLine(" Line: " + n.i.line_number);
        cgh.gen("push\tebp"); cgh.gen("\r\n");
        cgh.gen("mov\tebp, esp"); cgh.gen("\r\n");
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
        // result of above should be in eax
        cgh.gen("mov\teax, " + retIntegerLiteral); cgh.gen("\r\n");//todo: remove me

        cgh.gen("mov\tesp, ebp"); cgh.gen("\r\n");
        cgh.gen("pop\tebp"); cgh.gen("\r\n");
        cgh.gen("ret"); cgh.gen("\r\n");
        cgh.gen("_TEXT\tENDS"); cgh.gen("\r\n");
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
        n.e.accept(this);
        cgh.gen("push\teax"); cgh.gen("\r\n");
        cgh.gen("call\t" + NAME_PROC_PRINT); cgh.gen("\r\n");
        cgh.gen("add\tesp,4"); cgh.gen("\r\n");
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
        MethodInfo mi = null;

        n.e.accept(this);

        if (retClassName.equals("this")) {
            mi = currCI.lookupMethod(n.i.s);
        }
        else {
            mi = symtable.lookup(retClassName).lookupMethod(n.i.s);
        }
        int offsetmeth = mi.ordinal;

        n.i.accept(this);
/*
        <push arguments from right to left> 	; (as needed)
        mov    ecx, [ebp+offsetobj]	  	; get pointer to object
        mov    eax, [ecx]		  	; get pointer to vtable
        call     dword ptr [eax+offsetmeth]  	; call indirect via vtable
        <pop arguments>		  	; (if needed)
        mov    ecx, [ebp+offsetecxtemp] 		; (restore if needed)
*/

        for (int i = 0; i < n.el.size(); i++) {
            n.el.get(i).accept(this);
            if (i + 1 < n.el.size()) {
            }
        }
        // finally push the 'this' pointer
        // make sure 'this' is in ecx
        cgh.gen("push\tecx"); cgh.gen("\r\n");

        cgh.genCommentLine("Line " + Integer.toString(n.i.line_number));
        // <push arguments from right to left> 	; (as needed)

        //cgh.gen("mov\tecx, [ebp+offsetobj]"); cgh.gen("\r\n");//	  	; get pointer to object
        cgh.gen("mov\teax, [ecx]"); cgh.gen("\r\n");//		  	; get pointer to vtable
        cgh.gen("call\tdword ptr [eax+"+ offsetmeth + "]"); cgh.gen("\r\n");//  	; call indirect via vtable
        //<pop arguments>		  	; (if needed)
        //cgh.gen("mov\tecx, [ebp+offsetecxtemp]"); cgh.gen("\r\n");// 		; (restore if needed) todo: open?
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
        retClassName = "this";
    }

    // Exp e;
    public void visit(NewArray n) {

        n.e.accept(this);

    }

    // Identifier i;
    public void visit(NewObject n) {
        cgh.genCommentLine("Line " + Integer.toString(n.i.line_number));

        int numBytes = typesys.lookup(n.i.s).bytes + 4; //+ 4 for the this pointer
        String vtableAddress = symtable.lookup(n.i.s).vtName;

/*
        push   numBytes		; size-of-B + 4
        call	   malloc			; addr of bits returned in eax
        add     esp, 4			; pop numBytes

        lea	      edx, B$$		; get vtable address
        mov    [eax], edx		; store vtable pointer into 1st object slot
        mov    ecx, eax		; set up this for constructor
        push   ecx			; save ecx (constructor might clobber it)
        <push constructor arguments>	; arguments (if needed)
        call     B$B			; call constructor (no vtable lookup needed)
        <pop constructor arguments>	; (if needed)
        pop    eax			; recover pointer to object
        mov   [ebp+offsetb],eax		; store object reference in variable b
*/
        cgh.gen("push\t" + Integer.toString(numBytes)); cgh.gen("\r\n");
        cgh.gen("call\t" + NAME_PROC_MALLOC); cgh.gen("\r\n");
        cgh.gen("add\tesp, 4"); cgh.gen("\r\n");
        cgh.gen("lea\tedx, " + vtableAddress); cgh.gen("\r\n");
        cgh.gen("mov\t[eax], edx"); cgh.gen("\r\n");
        cgh.gen("mov\tecx, eax"); cgh.gen("\r\n");
        //cgh.gen("push\tecx"); cgh.gen("\r\n");
        //<push constructor arguments>	; arguments (if needed)
        //cgh.gen("call     B$B			; call constructor (no vtable lookup needed)
        //<pop constructor arguments>	; (if needed)
        //cgh.gen("pop\teax"); cgh.gen("\r\n");
        //cgh.gen("mov\t[ebp+offsetb],eax"); cgh.gen("\r\n");
        retClassName = n.i.s;
    }

    // Exp e;
    public void visit(Not n) {

        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {

    }
}
