class BadContravariantParams {
    public static void main(String[] args) {
        System.out.println(new B().my(432));
    }
}

class B
{
    int x1;
    int x2;
    public int my(int y)
    {
        int z;

        x1 = y;
        System.out.println(x1);

        x2 = 555 + x1;
        System.out.println(x2);
        return y;
    }
}


/*
class B
{
    public int my()
    {
        System.out.println(1+0);
        System.out.println(3-1);
        System.out.println(1*2*6);
        return (10-9);
    }
    public int my2()
    {
        System.out.println(2*2-1);
        System.out.println(2*2-1);
        System.out.println(2-1);
        return (10*100-9+4);
    }
}
*/

/*
class D extends B
{
    public int myFunc()
    {
        return 8;
    }
    public int funcBarr()
    {
        return 7;
    }
}

*/
