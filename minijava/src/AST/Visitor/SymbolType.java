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

class BaseSymType extends SymbolType
{
    BaseSymType()
    {
        idType = IdentifierType.ID_TYPE_VALUE;
    }
}

class CompoundSymType extends SymbolType
{
    public CompoundSymType()
    {
        idType = IdentifierType.ID_TYPE_REFERENCE;
    }
}

class MethodSymType extends SymbolType
{
    public SymbolType returnType;
    public ArrayList <SymbolType> paramListType;
    public MethodSymType()
    {
        idType = IdentifierType.ID_TYPE_REFERENCE;
    }
}

class IntSymType extends BaseSymType{}
class BooleanSymType extends BaseSymType{}
class VoidSymType extends BaseSymType{}

class UnknownSymType extends BaseSymType{}

class LiteralSymType extends BaseSymType
{
    public SymbolType valueType;
}

class ClassSymType extends CompoundSymType
{
    public SymbolType baseClassType;
    public List<SymbolType> fields;
    public List<SymbolType> methods;
}

class ArraySymType extends CompoundSymType
{
    public int dim;
    public SymbolType elementType;
}

