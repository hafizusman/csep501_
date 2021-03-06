package AST.Visitor;

import AST.*;

// Sample print visitor from MiniJava web site with small modifications for UW CSE.
// HP 10/11

public class ASTVisitor implements Visitor {
  private Integer IndentLevel = 0;

  private void Indent()
  {
      for(int i = 0; i < this.IndentLevel; i++)
          System.out.print("  ");
  }

  // Display added for toy example language.  Not used in regular MiniJava
  public void visit(Display n) {
    System.out.print("display ");
    n.e.accept(this);
    System.out.print(";");
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    int localindent = 0;
    System.out.print("Program(" + n.line_number + ")\n");
    this.IndentLevel++;
    Indent();
    n.m.accept(this);

    if (n.cl != null) {
        System.out.print("\n");
        this.IndentLevel++;
        localindent = this.IndentLevel;
        for (int i = 0; i < n.cl.size(); i++) {
            Indent();
            n.cl.get(i).accept(this);
            this.IndentLevel = localindent;
        }
    }
  }

  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    int localindent = 0;
    System.out.print("MainClass(" + n.line_number + ")\n");
    this.IndentLevel++;
    Indent();
    System.out.print("MainClass class name: ");
    n.i1.accept(this);
    System.out.print("\n");

    Indent();
    System.out.print("MainClass main formal list: int "); //int is assumed
    n.i2.accept(this);
    System.out.print("\n");

    Indent();
    System.out.print("MainClass main body: ");
    System.out.print("\n");
    this.IndentLevel++;
    Indent();
    n.s.accept(this);

    this.IndentLevel = 1; // hack
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    int localindent = 0;
    System.out.print("ClassDeclSimple(" + n.line_number + ")\n");
    this.IndentLevel++;
    localindent = this.IndentLevel;
    this.Indent();
    System.out.print("ClassDeclSimple class name: ");
    n.i.accept(this);


    if (n.vl != null) {
        System.out.print("\n");
        Indent();
        System.out.print("ClassDeclSimple Vars(" + n.vl.line_number + "):\n");
        this.IndentLevel++;
        localindent = this.IndentLevel;
        for (int i = 0; i < n.vl.size(); i++) {
            this.IndentLevel = localindent;
            Indent();
            n.vl.get(i).accept(this);
            if (i + 1 < n.vl.size()) {
                System.out.print("\n");
            }
        }
        this.IndentLevel--;
        this.IndentLevel--;
        localindent = this.IndentLevel;
    }


    this.IndentLevel = localindent;
    if (n.ml != null) {
        System.out.print("\n");
        Indent();
        System.out.print("ClassDeclSimple Methods(" + n.ml.line_number + "):\n");
        this.IndentLevel++;
        localindent = this.IndentLevel;
        for (int i = 0; i < n.ml.size(); i++) {
            this.IndentLevel = localindent;
            System.out.print("\n");
            Indent();
            n.ml.get(i).accept(this);
        }
    }
      this.IndentLevel = 1; // hack
  }

  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
      int localindent = 0;
      System.out.print("ClassDeclExtends(" + n.line_number + ")\n");
      this.IndentLevel++;
      localindent = this.IndentLevel;
      this.Indent();
      System.out.print("ClassDeclExtends class name: ");
      n.i.accept(this);
      System.out.print("\n");
      this.IndentLevel++;
      Indent();
      System.out.print(" extends: ");
      n.j.accept(this);
      this.IndentLevel--;

      if (n.vl != null) {
          System.out.print("\n");
          Indent();
          System.out.print("ClassDeclExtends Vars(" + n.vl.line_number + "):\n");
          this.IndentLevel++;
          localindent = this.IndentLevel;
          for (int i = 0; i < n.vl.size(); i++) {
              this.IndentLevel = localindent;
              Indent();
              n.vl.get(i).accept(this);
              if (i + 1 < n.vl.size()) {
                  System.out.print("\n");
              }
          }
          this.IndentLevel--;
          this.IndentLevel--;
          localindent = this.IndentLevel;
      }


      this.IndentLevel = localindent;
      if (n.ml != null) {
          System.out.print("\n");
          Indent();
          System.out.print("ClassDeclExtends Methods(" + n.ml.line_number + "):\n");
          this.IndentLevel++;
          localindent = this.IndentLevel;
          for (int i = 0; i < n.ml.size(); i++) {
              this.IndentLevel = localindent;
              System.out.print("\n");
              Indent();
              n.ml.get(i).accept(this);
          }
      }
      System.out.print("\n");
      this.IndentLevel = 1; // hack
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
      System.out.print("VarDecl(" + n.line_number + ")\n");
      this.IndentLevel++;
      this.Indent();
      n.t.accept(this);
      System.out.print(" ");
      n.i.accept(this);
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    int localindent = 0;
    System.out.print("MethodDecl(" + n.line_number + ")\n");

    this.IndentLevel++;
    localindent = this.IndentLevel;
    Indent();
    System.out.print("MethodDecl type: ");
    n.t.accept(this);
    System.out.print("\n");

    Indent();
    System.out.print("MethodDecl name: ");
    n.i.accept(this);
    System.out.print("\n");

    Indent();
    System.out.print("Formal List: \n");
    if (n.fl != null) {
        this.IndentLevel++;
        localindent = this.IndentLevel;

        for ( int i = 0; i < n.fl.size(); i++ ) {
            Indent();
            n.fl.get(i).accept(this);
            if (i+1 < n.fl.size()) {
                System.out.print("\n");
            }
            this.IndentLevel = localindent;
        }
        this.IndentLevel--;
        localindent = this.IndentLevel;
    }
    System.out.print("\n");

    if (n.vl != null) {
        for (int i = 0; i < n.vl.size(); i++) {
            Indent();
            n.vl.get(i).accept(this);
            System.out.print("\n");
            this.IndentLevel = localindent;
        }
    }

    if (n.sl != null) {
        for (int i = 0; i < n.sl.size(); i++) {
            Indent();
            n.sl.get(i).accept(this);
            this.IndentLevel = localindent;
            System.out.print("\n");
        }
    }

    Indent();
    System.out.print("ReturnExpr(" + n.e.line_number + ")\n");
    this.IndentLevel++;
    Indent();
    n.e.accept(this);
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    n.t.accept(this);
    System.out.print(" ");
    n.i.accept(this);
  }

  public void visit(IntArrayType n) {
    System.out.print("int []");
  }

  public void visit(BooleanType n) {
    System.out.print("boolean");
  }

  public void visit(IntegerType n) {
    System.out.print("int");
  }

  // String s;
  public void visit(IdentifierType n) {
    System.out.print(n.s);
  }

  // StatementList sl;
  public void visit(Block n) {
    System.out.print("Block(" + n.line_number + ")\n");
    this.IndentLevel++;

    int localindent = this.IndentLevel;
    for (int i = 0; i < n.sl.size(); i++) {
        this.IndentLevel = localindent; // cuz subnodes might change the indentation
        Indent();
        n.sl.get(i).accept(this);
        if (i < n.sl.size()-1) {
            System.out.print("\n");
        }
    }
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    int localindent = 0;
    System.out.print("If(" + n.line_number + ")\n");
    this.IndentLevel++;
    localindent = this.IndentLevel;

    Indent();
    System.out.print("Condition:\n");
    this.IndentLevel++;
    Indent();
    n.e.accept(this);
    System.out.print("\n");

      if (n.s1 != null) {
          this.IndentLevel = localindent;
          Indent();
          System.out.print("ThenExpr:\n");
          this.IndentLevel++;
          Indent();
          n.s1.accept(this);
          System.out.print("\n");
      }

      if(n.s2 != null) {
          this.IndentLevel = localindent;
          Indent();
          System.out.print("ElseExpr:\n");
          this.IndentLevel++;
          Indent();
          n.s2.accept(this);
      }
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    System.out.print("While(" + n.line_number + ")\n");

    this.IndentLevel++;
    this.Indent();
    n.e.accept(this);
    System.out.print("\n");
    if (n.s != null) {
        this.Indent();
        n.s.accept(this);
    }
  }

  // Exp e;
  public void visit(Print n) {
    System.out.print("System.out.println(" + n.line_number + ")\n");
    this.IndentLevel++; this.Indent();
    n.e.accept(this);
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    System.out.print("Assign(" + n.line_number + ")\n");

    this.IndentLevel++;
    this.Indent();
    n.i.accept(this);
    System.out.print("\n");

    this.Indent();
    n.e.accept(this);
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    System.out.print("ArrayAssign(" + n.line_number + ")\n");
    this.IndentLevel++;
    this.Indent();
    n.i.accept(this);
    System.out.print("\n");

    this.Indent();
    n.e1.accept(this);
    System.out.print("\n");

    this.Indent();
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(And n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" && ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" < ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" + ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" - ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Times n) {
    System.out.print("(");
    n.e1.accept(this);
    System.out.print(" * ");
    n.e2.accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    System.out.print("ArrayLookup(" + n.line_number + ")\n");

    this.IndentLevel++; this.Indent();
    n.e1.accept(this);
    System.out.print("\n");

    this.Indent();
    n.e2.accept(this);
  }

  // Exp e;
  public void visit(ArrayLength n) {
    System.out.print("ArrayLength(" + n.line_number + ")\n");
    this.IndentLevel++;
    this.Indent();
    n.e.accept(this);
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    int mylocalindent = 0;

    System.out.print("Call(" + n.line_number + ")\n");
    this.IndentLevel++;
    Indent();
    n.e.accept(this);
    System.out.print("\n");

    Indent();
    n.i.accept(this);

    if (n.el != null) {
        System.out.print("\n");
        this.IndentLevel++;
        mylocalindent = this.IndentLevel;

        for (int i = 0; i < n.el.size(); i++) {
            this.IndentLevel = mylocalindent; //cuz the sub-nodes might increase indentation
            Indent();
            n.el.get(i).accept(this);
            if (i + 1 < n.el.size()) {
                System.out.print("\n");
            }
        }
    }
  }

  // int i;
  public void visit(IntegerLiteral n) {
    System.out.print(n.i);
  }

  public void visit(True n) {
    System.out.print("true");
  }

  public void visit(False n) {
    System.out.print("false");
  }

  // String s;
  public void visit(IdentifierExp n) {
    System.out.print(n.s);
  }

  public void visit(This n) {
    System.out.print("this");
  }

  // Exp e;
  public void visit(NewArray n) {
    System.out.print("NewArray(" + n.line_number + ")\n");
    this.IndentLevel++;
    Indent();
    n.e.accept(this);
  }

  // Identifier i;
  public void visit(NewObject n) {
    System.out.print("NewObject(" + n.line_number + ")\n");
    this.IndentLevel++;
    Indent();
    System.out.print(n.i.s);
  }

  // Exp e;
  public void visit(Not n) {
    System.out.print("Not(" + n.line_number + ")\n");
    this.IndentLevel++;
    this.Indent();
    n.e.accept(this);
  }

  // String s;
  public void visit(Identifier n) {
    System.out.print(n.s);
  }
}
