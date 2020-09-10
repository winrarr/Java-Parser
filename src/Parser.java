import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final HashMap<String, Function<String, Literal>> literalPatterns = new HashMap<>() {{
        put("\\d+", x -> new IntLit(Integer.parseInt(x)));
        put("\\D+", StringLit::new);
    }};

    private static final HashMap<String, Supplier<Type>> typePatterns = new HashMap<>() {{
        put("int", IntType::new);
        put("string", StringType::new);
    }};

    private static final HashMap<String, Function<String, ParserExp>> expPatterns = new HashMap<>() {{
        put("\\{ .* \\}", ParserBlockExp::new);
        put("[a-zA-Z]+ [a-zA-Z]+ = [^;]+", ParserVarDecl::new);
        put("[a-zA-Z]+ = [^;]+", ParserAssignment::new);
    }};

    public static Exp parse(String program) throws NoMatchFoundException {
        return new ParserBlockExp(program).encode();
    }

    static class RegexMatcher {
        public String string;
        public String lastMatch;

        public RegexMatcher(String string) {
            this.string = string;
            lastMatch = null;
        }

        public boolean hasNext() { return !string.isBlank(); }

        public boolean hasNextUntil(String regex) { return !Pattern.compile(regex).matcher(string).lookingAt(); }

        public void skip(int places) {
            string = string.substring(places);
        }

        public <T> T matchNextPatternF(Map<String, Function<String, T>> patterns) throws NoMatchFoundException {
            for (Map.Entry<String, Function<String, T>> pattern : patterns.entrySet()) {
                Matcher m = Pattern.compile(pattern.getKey()).matcher(string);
                if (m.lookingAt()) {
                    lastMatch = m.group();
                    string = string.substring(lastMatch.length());
                    return pattern.getValue().apply(lastMatch);
                }
            }
            throw new NoMatchFoundException();
        }

        public <T> T matchNextPatternF(String s, Function<String, T> f) throws NoMatchFoundException {
            Matcher m = Pattern.compile(s).matcher(string);
            if (m.lookingAt()) {
                lastMatch = m.group();
                string = string.substring(lastMatch.length());
                return f.apply(lastMatch);
            }
            throw new NoMatchFoundException();
        }

        public <T> T matchNextPatternS(Map<String, Supplier<T>> patterns) throws NoMatchFoundException {
            for (Map.Entry<String, Supplier<T>> pattern : patterns.entrySet()) {
                Matcher m = Pattern.compile(pattern.getKey()).matcher(string);
                if (m.lookingAt()) {
                    lastMatch = m.group();
                    string = string.substring(lastMatch.length());
                    return pattern.getValue().get();
                }
            }
            throw new NoMatchFoundException();
        }
    }

    static class NoMatchFoundException extends Exception { }

    static abstract class ParserExp {
        String string;
        public ParserExp(String string) { this.string = string; }
        abstract AstNode encode() throws NoMatchFoundException;
    }


    static class ParserBlockExp extends ParserExp {
        public ParserBlockExp(String string) { super(string); }

        @Override
        Exp encode() throws NoMatchFoundException {
            ArrayList<VarDecl> vars = new ArrayList<>();
            ArrayList<DefDecl> defs = new ArrayList<>();
            ArrayList<ClassDecl> classes = new ArrayList<>();
            ArrayList<Exp> exps = new ArrayList<>();
            RegexMatcher rm = new RegexMatcher(string);
            rm.skip(2);
            while (rm.hasNextUntil(" }")) {
                AstNode node = rm.matchNextPatternF(expPatterns).encode();

                if (node.getClass() == VarDecl.class) {
                    vars.add((VarDecl) node);
                } else if (node.getClass() == DefDecl.class) {
                    defs.add((DefDecl) node);
                } else if (node.getClass() == ClassDecl.class) {
                    classes.add((ClassDecl) node);
                } else if (node.getClass() == Exp.class) {
                    exps.add((Exp) node);
                }

                rm.skip(2);
            }
            return new BlockExp(vars, defs, classes, exps);
        }
    }

    static abstract class ParserDecl {
        String string;
        public ParserDecl(String string) { this.string = string; }
        abstract Decl encode() throws NoMatchFoundException;
    }

    static class ParserVarDecl extends ParserExp {
        public ParserVarDecl(String string) {
            super(string);
        }

        @Override
        AstNode encode() throws NoMatchFoundException {
            RegexMatcher rm = new RegexMatcher(string);
            Type type = rm.matchNextPatternS(Parser.typePatterns);
            rm.skip(1);
            String id = rm.matchNextPatternF("[a-zA-Z]+", x -> x);
            rm.skip(3);
            Literal value = rm.matchNextPatternF(literalPatterns);
            return new VarDecl(id, type, value);
        }
    }

    static class ParserAssignment extends ParserExp {
        public ParserAssignment(String string) {
            super(string);
        }

        @Override
        Exp encode() throws NoMatchFoundException {
            RegexMatcher rm = new RegexMatcher(string);
            String id = rm.matchNextPatternF("[a-zA-Z]+", x -> x);
            rm.skip(3);
            Literal value = rm.matchNextPatternF(literalPatterns);
            return new AssignmentExp(id, value);
        }
    }
}