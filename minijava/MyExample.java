class BadContravariantParams {
    public static void main(String[] args) {
<<<<<<< HEAD
        System.out.println(new B().my(432));
=======
        System.out.println(new B().my(false));
>>>>>>> parent of 4a18511... can read/write fields from our own class
    }
}

class B
{
<<<<<<< HEAD
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
=======
>>>>>>> parent of 4a18511... can read/write fields from our own class
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
<<<<<<< HEAD
*/
=======

>>>>>>> parent of 4a18511... can read/write fields from our own class
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
