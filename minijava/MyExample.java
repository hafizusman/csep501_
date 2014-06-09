class BadContravariantParams {
    public static void main(String[] args) {
        System.out.println(new B().my());
    }
}

class B
{
    public int my()
    {
        return 9;
    }
    public int my2()
    {
        return 10;
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