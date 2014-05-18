package AST.Visitor;

import AST.*;

// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// HP 10/11

public class TypeSystemVisitor implements Visitor {
  private SymbolTable symtable;
  private ClassInfo currentci;
  private FieldInfo currentfieldi;
  private MethodInfo currentmi;
  private FormalInfo currentformali;
  private LocalInfo currentlocali;
  private VarInfo currentvi;
  private SymbolType returnSymbolType;

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
    
    n.i1.accept(this);

    currentci = symtable.lookup(n.i1.s);
    currentci.type = TypeSystem.classt;

    currentmi = currentci.lookupMethod("main");
    currentmi.type = TypeSystem.methodt;
    currentmi.returnType = TypeSystem.voidt;  // main always returns void

    n.i2.accept(this);
    
    
    n.s.accept(this);
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    
    n.i.accept(this);

    currentci = symtable.lookup(n.i.s);
    currentci.type = TypeSystem.classt;

    for ( int i = 0; i < n.vl.size(); i++ ) {
        currentfieldi = currentci.lookupField(n.vl.get(i).i.s);
        currentvi = currentfieldi;
        n.vl.get(i).accept(this);
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
        currentmi = currentci.lookupMethod(n.ml.get(i).i.s);
        currentmi.type = TypeSystem.methodt;
        n.ml.get(i).accept(this);
    }
    
    
  }
 
  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    
    n.i.accept(this);

    currentci = symtable.lookup(n.i.s);
    currentci.type = TypeSystem.classt;

    n.j.accept(this);
      // todo: setup return type from type system by lookinup
    
    for ( int i = 0; i < n.vl.size(); i++ ) {
        currentfieldi = currentci.lookupField(n.vl.get(i).i.s);
        currentvi = currentfieldi;
        n.vl.get(i).accept(this);
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
        currentmi = currentci.lookupMethod(n.ml.get(i).i.s);
        currentmi.type = TypeSystem.methodt;
        n.ml.get(i).accept(this);
    }
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    n.t.accept(this);

    if (currentvi instanceof FieldInfo) {
        currentfieldi.type = returnSymbolType;
    }
    // formals handled in separate visit() function
    //else if (currentvi instanceof FormalInfo) {
    //    currentformali.type = returnSymbolType;
    //}
    else if (currentvi instanceof LocalInfo) {
        currentlocali.type = returnSymbolType;
    }
    else {
        throw new RuntimeException();
    }

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
    currentci.lookupMethod(n.i.s).returnType = returnSymbolType;

    n.i.accept(this);
    
    for ( int i = 0; i < n.fl.size(); i++ ) {
        currentformali = currentmi.lookupFormal(n.fl.get(i).i.s);
        currentvi = currentformali;
        n.fl.get(i).accept(this);
    }
    
    for ( int i = 0; i < n.vl.size(); i++ ) {
        currentlocali = currentmi.lookupLocal(n.vl.get(i).i.s);
        currentvi = currentlocali;
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

    if (currentvi instanceof FormalInfo) {
        currentformali.type = returnSymbolType;
    }
    else {
        throw new RuntimeException();
    }

    n.i.accept(this);
  }

  public void visit(IntArrayType n) {
      returnSymbolType = TypeSystem.arrayt;
  }

  public void visit(BooleanType n) {
      returnSymbolType = TypeSystem.boolt;
  }

  public void visit(IntegerType n) {
      returnSymbolType = TypeSystem.intt;
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
