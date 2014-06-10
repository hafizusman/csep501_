package AST;
import AST.Visitor.Visitor;

public class MethodDecl extends ASTNode {
  public Type t;
  public Identifier i;
  public FormalList fl;
  public VarDeclList vl;
  public StatementList sl;
  public Exp e;

  public MethodDecl(Type at, Identifier ai, FormalList afl, VarDeclList avl, 
                    StatementList asl, Exp ae, int ln) {
    super(ln);
    t=at;
    i=ai;

    if (afl!=null)
        fl=afl;
    else
        fl = new FormalList(LINENUM_UNDEFINED);

    if (avl!=null)
        vl=avl;
    else
        vl = new VarDeclList(LINENUM_UNDEFINED);

    if (asl!=null)
        sl=asl;
    else
        sl = new StatementList(LINENUM_UNDEFINED);

    e=ae;
  }
 
  public void accept(Visitor v) {
    v.visit(this);
  }
}
