import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class Lit { }

class IntLit extends Lit {
    public final int value;
    public IntLit(int value) { this.value = value; }
}

class StringLit extends Lit {
    private final String value;
    public StringLit(String value) { this.value = value; }
}

abstract class Type { }
class IntType extends Type { }
class StringType extends Type { }

abstract class Node { }

abstract class ParserNode extends Node {
    String str;

    public void setStr(String str) {
        this.str = str;
    }

    abstract Exp encode() throws Parser.NoMatchFoundException;
}

abstract class Exp extends Node { }

class VarDeclP extends ParserNode {

    public VarDeclExp encode() throws Parser.NoMatchFoundException {
        HashMap<String, Supplier<Type>> typePatterns = new HashMap<>() {{
            put("int", IntType::new);
            put("string",StringType::new);
        }};

        // get type
        Parser.PatternMatch<Type> type = Parser.matchNextPattern(str, typePatterns);
        str = type.resultString.substring(1);

        // get id
        Matcher m = Pattern.compile("[a-zA-Z]+").matcher(str);
        m.find();
        String id = m.group();

        // reduce to lit expression
        str = str.substring(id.length()).replaceFirst(" *= *", "");

        return new VarDeclExp(id, type.obj, Parser.makeLit(str));
    }
}

class VarDeclExp extends Exp {
    public Type type;
    public String id;
    public Lit value;

    public VarDeclExp(String id, Type type, Lit value) {
        this.type = type;
        this.id = id;
        this.value = value;
    }
}

class Assignment extends ParserNode {

    public AssignmentExp encode() {

        Matcher m = Pattern.compile("[a-zA-Z]+").matcher(str);
        m.find();
        String id = m.group();

        str = str.replaceFirst("[a-zA-Z]+ = ", "");

        return new AssignmentExp(id, Parser.makeLit(str));
    }
}

class AssignmentExp extends Exp {
    public String id;
    public Lit value;

    public AssignmentExp(String id, Lit value) {
        this.id = id;
        this.value = value;
    }
}

public class Parser {
    private final HashMap<String, Supplier<ParserNode>> expPatterns = new HashMap<>() {{
        put("[a-zA-Z]+ [a-zA-Z]+ = [^;]+", VarDeclP::new);
        put("[a-zA-Z]+ = [^;]+", Assignment::new);
    }};

    public ArrayList<Exp> parse(String program) throws NoMatchFoundException {
        String prg = program.replaceAll("; *", "; ");
        ArrayList<ParserNode> parserNodes = new ArrayList<>();
        while (!prg.isBlank()) {
            PatternMatch<ParserNode> match = matchNextPattern(prg, expPatterns);
            prg = match.resultString.substring(2);
            match.obj.setStr(match.matchString);
            parserNodes.add(match.obj);
        }

        ArrayList<Exp> exps = new ArrayList<>();
        for (ParserNode node : parserNodes) {
            exps.add(node.encode());
        }

        return exps;
    }

    static class PatternMatch<T> {
        public T obj;
        public String matchString;
        public StringBuilder resultString;

        public PatternMatch(T obj, String matchString, StringBuilder resultString) {
            this.obj = obj;
            this.matchString = matchString;
            this.resultString = resultString;
        }
    }

    static class NoMatchFoundException extends Exception { }

    public static <T> PatternMatch<T> matchNextPattern(String str, Map<String, Supplier<T>> patterns) throws NoMatchFoundException {
        for (String pattern : patterns.keySet()) {
            Matcher m = Pattern.compile(pattern).matcher(str);
            if (m.lookingAt()) {
                StringBuilder sb = new StringBuilder();
                m.appendReplacement(sb, "");
                return new PatternMatch<>(patterns.get(pattern).get(), m.group(), m.appendTail(sb));
            }
        }
        throw new NoMatchFoundException();
    }

    public static Lit makeLit(String str) {
        if (str.matches("\\d+")) {
            return new IntLit(Integer.parseInt(str));
        } else if (str.matches("\\D+")) {
            return new StringLit(str);
        }
        throw new Error();
    }
}
