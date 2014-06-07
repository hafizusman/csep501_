package AST.Visitor;

/**
 * Created by Muhammad on 6/7/2014.
 */
public class CGHelper {
    private static void gen(String s)
    {
        System.out.print(s);
    }

    public static void genComment(String s)
    {
        gen(";" + s.toString());
        gen("\n");
    }
}
