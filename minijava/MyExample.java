
class BadContravariantParams {
    public static void main(String[] args) {
        System.out.println(123);
    }
}

class SuperThing {
    int v;

    public int getV() {
        return v;
    }

    public int setV(int value) {
        v = value;
        return v;
    }
}

class Thing extends SuperThing {
}

class A {
    public SuperThing m(Thing s) {
        return s;
    }
}

class B extends A {
    public Thing m(SuperThing s) {
        return new Thing();
    }
}