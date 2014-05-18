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
}

class IntSymbolType extends BaseSymbolType{}
class BooleanSymbolType extends BaseSymbolType{}
class VoidSymbolType extends BaseSymbolType{}

class UnknownSymbolType extends BaseSymbolType{}

class LiteralSymbolType extends BaseSymbolType
{
    public SymbolType valueType;
}

class ClassSymbolType extends CompoundSymbolType
{
    public SymbolType baseClassType;
    public List<SymbolType> fields;
    public List<SymbolType> methods;
}

class ArraySymbolType extends CompoundSymbolType
{
    public int dim;
    public SymbolType elementType;
}

