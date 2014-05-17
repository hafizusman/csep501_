package AST;
import AST.Visitor.Visitor;

public class ClassDeclSimple extends ClassDecl {
  public Identifier i;
  public VarDeclList vl;  
  public MethodDeclList ml;
 
  public ClassDeclSimple(Identifier ai, VarDeclList avl, MethodDeclList aml, int ln) {
    super(ln);
    i=ai;
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
