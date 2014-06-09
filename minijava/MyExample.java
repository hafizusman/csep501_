class BadContravariantParams {
    public static void main(String[] args) {
        System.out.println(new B().my(false));
    }
}

class B
{
    public int my(boolean c)
    {
        int i;
        i = 3;

        while (i < 11) {
            if ( (i < 6) && (i < 5)) {
                if (!c) {
                    System.out.println(123);
                }
                else {
                    System.out.println(321);
                }
            }
            else {
                System.out.println(456);
            }
            i = i + 1;
        }

        return i;
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