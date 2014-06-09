class BadContravariantParams {
    public static void main(String[] args) {
        System.out.println(new B().my2());
    }
}

class B
{
    public int my()
    {
        System.out.println(2*2-1);
        return 9;
    }
    public int my2()
    {
        System.out.println(2-1);
        return (10*100-9+4);
    }
}

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