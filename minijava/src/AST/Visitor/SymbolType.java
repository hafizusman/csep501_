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
    public ArrayList <SymbolType> paramListType;
    public MethodSymbolType()
    {
        idType = IdentifierType.ID_TYPE_REFERENCE;
    }
    public String toString()
    {
        return "MethodSymbolType";
    }
}

class IntSymbolType extends BaseSymbolType
{
    public String toString()
    {
        return "IntSymbolType";
    }

}

class BooleanSymbolType extends BaseSymbolType
{
    public String toString()
    {
        return "BooleanSymbolType";
    }

}

class VoidSymbolType extends BaseSymbolType
{
    public String toString()
    {
        return "VoidSymbolType";
    }

}

class UnknownSymbolType extends BaseSymbolType
{
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
    public SymbolType baseClassType;
    public List<SymbolType> fields;
    public List<SymbolType> methods;
    public String toString()
    {
        return "ClassSymbolType";
    }

}

class ArraySymbolType extends CompoundSymbolType
{
    public int dim;
    public SymbolType elementType;
    public String toString()
    {
        return "ArraySymbolType";
    }
}

