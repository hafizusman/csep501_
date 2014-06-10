package AST.Visitor;

import java.util.List;
import java.util.ArrayList;


abstract public class SymbolType
{
    public enum IdentifierType
    {
        ID_TYPE_VALUE,
        ID_TYPE_REFERENCE
    }

    public IdentifierType idType;
    public int bytes;
}

class BaseSymbolType extends SymbolType
{
    BaseSymbolType()
    {
        idType = IdentifierType.ID_TYPE_VALUE;
    }
}

class CompoundSymbolType extends SymbolType
{
    public CompoundSymbolType()
    {
        idType = IdentifierType.ID_TYPE_REFERENCE;
    }
}

class MethodSymbolType extends CompoundSymbolType
{
    public SymbolType returnType;
    public String name;
    public ArrayList <SymbolType> paramListType;
    public MethodSymbolType()
    {
        returnType = null;
        idType = IdentifierType.ID_TYPE_REFERENCE;
        paramListType = new ArrayList<SymbolType>();
        name = null;
    }
    public String toString()
    {
        String s = "MethodSymbolType <" + returnType.toString() + ">";

        for (int i  = 0; i < paramListType.size(); i++) {
            s += (" " + paramListType.get(i).toString());
        }

        return s;
    }
}

class IntSymbolType extends BaseSymbolType
{
    public IntSymbolType()
    {
        super();
        bytes = 4;
    }
    public String toString()
    {
        return "IntSymbolType";
    }

}

class BooleanSymbolType extends BaseSymbolType
{
    public BooleanSymbolType()
    {
        super();
        bytes = 4;
    }
    public String toString()
    {
        return "BooleanSymbolType";
    }

}

class VoidSymbolType extends BaseSymbolType
{
    public VoidSymbolType()
    {
        super();
        bytes = 4; //todo: change?
    }
    public String toString()
    {
        return "VoidSymbolType";
    }

}

class UnknownSymbolType extends BaseSymbolType
{
    public UnknownSymbolType()
    {
        super();
        bytes = 0; //todo: change?
    }
    public String toString()
    {
        return "UnknownSymbolType";
    }
}

class IdentifierSymbolType extends SymbolType
{
    public String toString()
    {
        return "IdentifierSymbolType";
    }
}

class LiteralSymbolType extends BaseSymbolType
{
    public SymbolType valueType;
}

class ClassSymbolType extends CompoundSymbolType
{
    public String name;
    public SymbolType baseClassType;
    public ArrayList<SymbolType> fields;
    public ArrayList<SymbolType> methods;
    public ClassSymbolType()
    {
        super();
        fields = new ArrayList<SymbolType>();
        methods = new ArrayList<SymbolType>();
        baseClassType = null;
        name = null;
        bytes = -1; //will be computed during bring-up of type system
    }

    public String toString()
    {
        return ("ClassSymbolType: " + name);
    }

}

class ArraySymbolType extends CompoundSymbolType
{
    public int dim;
    public ArraySymbolType()
    {
        super();
        bytes = 4;
    }
    public SymbolType elementType;
    public String toString()
    {
        return "ArraySymbolType";
    }
}

