package AST.Visitor;

import AST.*;

import java.util.HashMap;

public class SymbolTableVisitor implements Visitor
{
  private FieldInfo currentFI;
  private LocalInfo currentLI;
  private VarInfo currentVI; // REMEMBER to set this before calling accept!
  private ClassInfo currentCI;
  private MethodInfo currentMI;
  public SymbolTable symtable;

  // Display added for toy example language.  Not used in regular MiniJava
  public void visit(Display n) {
    
    n.e.accept(this);
    
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n)
  {
    symtable  = new SymbolTable();

    currentCI = new ClassInfo();

    n.m.accept(this);

    for ( int i = 0; i < n.cl.size(); i++ ) {
        currentCI = new ClassInfo();
        n.cl.get(i).accept(this);
    }
  }

  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n)
  {
    n.i1.accept(this);
    currentCI.ln = n.i1.line_number;
    symtable.enter(n.i1.s, currentCI);

    currentMI = new MethodInfo(n.i2.line_number); //line number is a guess since main keyword doesn't return line number
    currentCI.enterMethod("main", currentMI); //main is implied

    n.i2.accept(this); // todo: add this String to the formal list

    n.s.accept(this);
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n)
  {
    n.i.accept(this);
    currentCI.ln = n.i.line_number;
    symtable.enter(n.i.s, currentCI);

    for ( int i = 0; i < n.vl.size(); i++ ) {
        currentFI = new FieldInfo();
        currentVI = currentFI;
        n.vl.get(i).accept(this);
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
        currentMI = new MethodInfo();
        n.ml.get(i).accept(this);
    }
  }

  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n)
  {
    n.i.accept(this);
    currentCI.ln = n.i.line_number;
    symtable.enter(n.i.s, currentCI);

    n.j.accept(this);
    currentCI.baseClass = n.j.s;

    for ( int i = 0; i < n.vl.size(); i++ ) {
        currentFI = new FieldInfo();
        currentVI = currentFI;
        n.vl.get(i).accept(this);
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
        currentMI = new MethodInfo();
        n.ml.get(i).accept(this);
    }
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    n.t.accept(this);

    n.i.accept(this);
    currentVI.ln = n.i.line_number;
    if(currentVI instanceof FieldInfo)
        currentCI.enterField(n.i.s, (FieldInfo)currentVI);
    else if (currentVI instanceof LocalInfo)
        currentMI.enterLocal(n.i.s, (LocalInfo)currentVI);
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
    currentMI.ln = n.i.line_number;
    currentCI.enterMethod(n.i.s, currentMI);

    for ( int i = 0; i < n.fl.size(); i++ ) {
        currentLI = new LocalInfo();
        currentVI = currentLI;
        n.fl.get(i).accept(this);
    }
    
    for ( int i = 0; i < n.vl.size(); i++ ) {
        currentLI = new LocalInfo();
        currentVI = currentLI;
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
    currentVI.ln = n.i.line_number;
    if(currentVI instanceof FieldInfo)
        throw new RuntimeException(); //shouldn't happen! formal should always be LocalInfo or the like...
        //currentCI.enterField(n.i.s, (FieldInfo)currentVI);
    else if (currentVI instanceof LocalInfo)
        currentMI.enterLocal(n.i.s, (LocalInfo)currentVI);

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
