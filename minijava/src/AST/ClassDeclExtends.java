package AST;
import AST.Visitor.Visitor;

public class ClassDeclExtends extends ClassDecl {
  public Identifier i;
  public Identifier j;
  public VarDeclList vl;  
  public MethodDeclList ml;
 
  public ClassDeclExtends(Identifier ai, Identifier aj, 
                  VarDeclList avl, MethodDeclList aml, int ln) {
    super(ln);
    i=ai;
      j=aj;

      if (avl != null)
          vl=avl;
      else
          vl = new VarDeclList(LINENUM_UNDEFINED);

      if (aml != null)
          ml=aml;
      else
          ml = new MethodDeclList(LINENUM_UNDEFINED);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
