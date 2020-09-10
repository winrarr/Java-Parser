import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ast { }

abstract class AstNode { }


abstract class Exp extends AstNode {
    abstract Val eval();
}

class VarExp extends Exp {
    String id;
    Type type;
    Literal value;

    public VarExp(String id, Type type, Literal value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    @Override
    Val eval() {
        return null;
    }
}

class BinOpExp extends Exp {
    Exp leftExp;
    BinOp op;
    Exp rightExp;

    public BinOpExp(Exp leftExp, BinOp op, Exp rightExp) {
        this.leftExp = leftExp;
        this.op = op;
        this.rightExp = rightExp;
    }

    @Override
    Val eval() {
        return null;
    }
}

class UnOpExp extends Exp {
    Exp exp;
    BinOp op;

    public UnOpExp(Exp exp, BinOp op) {
        this.exp = exp;
        this.op = op;
    }

    @Override
    Val eval() {
        return null;
    }
}

class IfThenElseExp extends Exp {
    Exp condExp;
    Exp thenExp;
    Exp elseExp;

    public IfThenElseExp(Exp condExp, Exp thenExp, Exp elseExp) {
        this.condExp = condExp;
        this.thenExp = thenExp;
        this.elseExp = elseExp;
    }

    @Override
    Val eval() {
        return null;
    }
}

class BlockExp extends Exp {
    ArrayList<VarDecl> vars;
    ArrayList<DefDecl> defs;
    ArrayList<ClassDecl> classes;
    ArrayList<Exp> exps;

    public BlockExp(ArrayList<VarDecl> vars, ArrayList<DefDecl> defs, ArrayList<ClassDecl> classes, ArrayList<Exp> exps) {
        this.vars = vars;
        this.defs = defs;
        this.classes = classes;
        this.exps = exps;
    }

    @Override
    Val eval() {
        return null;
    }
}

class TupleExp extends Exp {
    ArrayList<Exp> exps;

    public TupleExp(ArrayList<Exp> exps) {
        this.exps = exps;
    }

    @Override
    Val eval() {
        return null;
    }
}

class MatchExp extends Exp {
    Exp exp;
    ArrayList<MatchCase> cases;

    public MatchExp(Exp exp, ArrayList<MatchCase> cases) {
        this.exp = exp;
        this.cases = cases;
    }

    @Override
    Val eval() {
        return null;
    }
}

class CallExp extends Exp {
    Exp funExp;
    ArrayList<Exp> args;

    public CallExp(Exp funExp, ArrayList<Exp> args) {
        this.funExp = funExp;
        this.args = args;
    }

    @Override
    Val eval() {
        return null;
    }
}

class LambdaExp extends Exp {
    ArrayList<FunParam> params;
    Exp body;

    public LambdaExp(ArrayList<FunParam> params, Exp body) {
        this.params = params;
        this.body = body;
    }

    @Override
    Val eval() {
        return null;
    }
}

class AssignmentExp extends Exp {
    String id;
    Exp exp;

    public AssignmentExp(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    Val eval() {
        return null;
    }
}

class WhileExp extends Exp {
    Exp cond;
    Exp body;

    public WhileExp(Exp cond, Exp body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    Val eval() {
        return null;
    }
}

class DoWhileExp extends Exp {
    Exp cond;
    Exp body;

    public DoWhileExp(Exp cond, Exp body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    Val eval() {
        return null;
    }
}

class NewObjExp extends Exp {
    String id;
    ArrayList<Exp> args;

    public NewObjExp(String id, ArrayList<Exp> args) {
        this.id = id;
        this.args = args;
    }

    @Override
    Val eval() {
        return null;
    }
}

class LookupExp extends Exp {
    Exp objExp;
    String memberId;

    public LookupExp(Exp objExp, String memberId) {
        this.objExp = objExp;
        this.memberId = memberId;
    }

    @Override
    Val eval() {
        return null;
    }
}


abstract class Literal extends Exp { }

class IntLit extends Literal {
    public final int value;
    public IntLit(int value) { this.value = value; }

    @Override
    Val eval() {
        return new IntVal(value);
    }
}

class BoolLit extends Literal {
    private final Boolean value;
    public BoolLit(Boolean value) { this.value = value; }

    @Override
    Val eval() {
        return new BoolVal(value);
    }
}

class FloatLit extends Literal {
    private final Float value;
    public FloatLit(Float value) { this.value = value; }

    @Override
    Val eval() {
        return new FloatVal(value);
    }
}

class StringLit extends Literal {
    private final String value;
    public StringLit(String value) { this.value = value; }

    @Override
    Val eval() {
        return new StringVal(value);
    }
}

class NullLit extends Literal {
    @Override
    Val eval() {
        return new RefVal(-1, new NullType());
    }
}


abstract class BinOp extends AstNode { }

class PlusBinOp extends BinOp { }

class MinusBinOp extends BinOp { }

class MultBinOp extends BinOp { }

class DivBinOp extends BinOp { }

class EqualBinOp extends BinOp { }

class LessThanBinOp extends BinOp { }

class LessThanOrEqualBinOp extends BinOp { }

class ModuloBinOp extends BinOp { }

class MaxBinOp extends BinOp { }

class AndBinOp extends BinOp { }

class OrBinOp extends BinOp { }

class AndAndBinOp extends BinOp { }

class OrOrBinOp extends BinOp { }


abstract class UnOp extends AstNode { }

class NegUnOp extends UnOp { }

class NotUnOp extends UnOp { }


abstract class Decl extends AstNode { }

class VarDecl extends Decl {
    public String id;
    public Type type;
    public Exp exp;

    public VarDecl(String id, Type type, Exp exp) {
        this.id = id;
        this.type = type;
        this.exp = exp;
    }
}

class DefDecl extends Decl {
    public String id;
    public ArrayList<FunParam> params;
    public Type resType;
    public Exp body;

    public DefDecl(String id, ArrayList<FunParam> params, Type resType, Exp body) {
        this.id = id;
        this.params = params;
        this.resType = resType;
        this.body = body;
    }
}

class ClassDecl extends Decl {
    public String id;
    public ArrayList<FunParam> params;
    public BlockExp body;

    public ClassDecl(String id, ArrayList<FunParam> params, BlockExp body) {
        this.id = id;
        this.params = params;
        this.body = body;
    }
}


class FunParam extends AstNode {
    public String id;
    public Type type;

    public FunParam(String id, Type type) {
        this.id = id;
        this.type = type;
    }
}


class MatchCase extends AstNode {
    public ArrayList<String> pattern;
    public Exp exp;

    public MatchCase(ArrayList<String> pattern, Exp exp) {
        this.pattern = pattern;
        this.exp = exp;
    }
}


abstract class Type { }

class IntType extends Type { }

class FloatType extends Type { }

class StringType extends Type { }

class TupleType extends Type {
    public ArrayList<Type> types;

    public TupleType(ArrayList<Type> types) {
        this.types = types;
    }
}

class FunType extends Type {
    public ArrayList<Type> paramTypes;
    public Type restype;

    public FunType(ArrayList<Type> paramTypes, Type restype) {
        this.paramTypes = paramTypes;
        this.restype = restype;
    }
}

class ClassNameType extends Type {
    public String id;

    public ClassNameType(String id) {
        this.id = id;
    }
}

class NullType extends Type { }


class Env extends HashMap<String, Val> { }

class ClassEnv extends HashMap<String, Constructor> { }


abstract class Val { }

class IntVal extends Val {
    public int value;
    public IntVal(int value) { this.value = value; }
}

class BoolVal extends Val {
    public boolean value;
    public BoolVal(boolean value) {
        this.value = value;
    }
}

class FloatVal extends Val {
    public float value;
    public FloatVal(float value) {
        this.value = value;
    }
}

class StringVal extends Val  {
    public String value;
    public StringVal(String value) {
        this.value = value;
    }
}

class TupleVal extends Val {
    public ArrayList<Val> values;
    public TupleVal(ArrayList<Val> values) {
        this.values = values;
    }
}

class ClosureVal extends Val {
    ArrayList<FunParam> params;
    Type type;
    Exp body;
    Env env;
    ClassEnv cEnv;
    ArrayList<DefDecl> defs;
    public ClosureVal(ArrayList<FunParam> params, Type type, Exp body, Env env, ClassEnv cEnv, ArrayList<DefDecl> defs) {
        this.params = params;
        this.type = type;
        this.body = body;
        this.env = env;
        this.cEnv = cEnv;
        this.defs = defs;
    }
}

class Constructor {
    ArrayList<FunParam> params;
    BlockExp body;
    Env env;
    ClassEnv cEnv;
    ArrayList<ClassDecl> classes;
    Position srcPos;

    public Constructor(ArrayList<FunParam> params, BlockExp body, Env env, ClassEnv cEnv, ArrayList<ClassDecl> classes, Position srcpos) {
        this.params = params;
        this.body = body;
        this.env = env;
        this.cEnv = cEnv;
        this.classes = classes;
        this.srcPos = srcpos;
    }
}

class DynamicClassType extends Type {
    Position srcPos;

    public DynamicClassType(Position srcPos) {
        this.srcPos = srcPos;
    }
}

class RefVal extends Val {
    int loc;
    Type type;

    public RefVal(int loc, Type type) {
        this.loc = loc;
        this.type = type;
    }

}

class ObjectVal extends Val { Map<String, Val> members;
    public ObjectVal(Map<String, Val> members) {
        this.members = members;
    }
}