class BadContravariantParams {
    public static void main(String[] args) {
        System.out.println(new B().my(2, false, (4+4)));
    }
}

class B
{
    public int my(int x, boolean y,  int z)
    {
        int a;
        int b;
        int i;
        boolean c;

        a = x+3*z;
        b = z;
        c = y;
        i = 5;

        while(0 < i) {
            i = i - 1;
            System.out.println(777);
        }
        return (a*z-b+1);
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