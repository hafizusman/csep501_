package AST;
import AST.Visitor.Visitor;

public class Program extends ASTNode {
  public MainClass m;
  public ClassDeclList cl;

  public Program(MainClass am, ClassDeclList acl, int ln) {
    super(ln);
    m=am;

    if (acl!=null)
        cl=acl;
    else
        cl=new ClassDeclList(LINENUM_UNDEFINED);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
