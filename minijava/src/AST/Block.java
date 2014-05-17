package AST;
import AST.Visitor.Visitor;

public class Block extends Statement {
  public StatementList sl;

  public Block(StatementList asl, int ln) {
    super(ln);
    if (asl != null)
        sl=asl;
    else
        sl = new StatementList(LINENUM_UNDEFINED);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}

