package c4_1_syntax_tree;

public class Parser {
    private int index;
    private String input;

    public Parser() {
    }
    public Parser(String input) {
        initialize(input);
    }

    public void initialize(String input) {
        index = 0;
        this.input = input;
    }

    private void throwErrorMessage(String message) {
        System.out.println("SyntaxError!");

        System.out.print(message);
        System.out.print(" at index ");
        System.out.println(index);

        System.out.println(input);

        for (int i = 0; i < index; i++) {
            System.out.print(" ");
        }
        System.out.println("^ here");

        throw new RuntimeException("SyntaxError!");
    }

    private boolean endOfInput() {
        return index >= input.length();
    }

    private boolean nextIs(char peek) {
        if (endOfInput()) {
            return false;
        }

        return input.charAt(index) == peek;
    }

    private boolean nextIsAlphaNumeric() {
        if (endOfInput()) {
            return false;
        }

        return Character.isLetterOrDigit(input.charAt(index));
    }

    private char next() {
        if (endOfInput()) {
            throwErrorMessage("Unexpected end of input");
        }

        return input.charAt(index++);
    }

    private void match(char expect) {
        if (!nextIs(expect)) {
            System.out.println("match failed!");
            throwErrorMessage("Expected '" + expect + "'");
        }

        next();
    }

    private void assertEndOfInput() {
        if (!endOfInput()) {
            throwErrorMessage("Expected end of input");
        }
    }

    //
    // Parse Methods:
    //

    public Visitable start() {
        System.out.println("Start " + index);
        if (nextIs('#')) {
            match('#');
            assertEndOfInput();
            return new OperandNode("#");
        }
        else if (nextIs('(')) {
            match('(');
            Visitable regexp = regExp(null);
            match(')');
            match('#');
            assertEndOfInput();

            return new BinOpNode("°", regexp, new OperandNode("#"));
        } else {
            throwErrorMessage("Expected # or (");
            throw new RuntimeException("Unreachable");
        }
    }

    private Visitable regExp(Visitable parameter) {
        System.out.println("RegExp " + index);
        return re(term(null));
    }

    private Visitable re(Visitable parameter) {
        System.out.println("Re " + index);
        if (nextIs('|')) {
            match('|');

            Visitable term = term(null);
            Visitable result = new BinOpNode("|", parameter, term);

            return re(result);
        }
        else if (nextIs(')')) {
            return parameter;
        }
        else {
            throwErrorMessage("Expected '|' or ')'");
            throw new RuntimeException("Unreachable");
        }
    }

    private Visitable term(Visitable parameter) {
        System.out.println("Term " + index);
        if (nextIs('(') || nextIsAlphaNumeric()) {
            Visitable factorTree = factor(null);

            if (parameter != null) {
                Visitable root = new BinOpNode("°", parameter, factorTree);
                return term(root);
            }

            return term(factorTree);
        }
        else if (nextIs('|') || nextIs(')')) {
            return parameter;
        }
        else {
            throwErrorMessage("Expected '(', char, '|' or ')'");
            throw new RuntimeException("Unreachable");
        }
    }

    private Visitable factor(Visitable parameter) {
        System.out.println("Factor " + index);
        if (nextIs('(') || nextIsAlphaNumeric()) {
            Visitable ret = elem(null);
            return hOp(ret);
        }
        else {
            throwErrorMessage("Expected '(' or char");
            throw new RuntimeException("Unreachable");
        }
    }

    private Visitable hOp(Visitable parameter) {
        System.out.println("HOp " + index);
        if (nextIs('*')) {
            match('*');
            return new UnaryOpNode("*", parameter);
        }
        else if (nextIs('+')) {
            match('+');
            return new UnaryOpNode("+", parameter);
        }
        else if (nextIs('?')) {
            match('?');
            return new UnaryOpNode("?", parameter);
        }
        else if (nextIs('(') || nextIs('|') || nextIs(')') || nextIsAlphaNumeric()) {
            return parameter;
        }
        else {
            throwErrorMessage("Expected '*', '+', '?', '(', '|', ')' or char");
            throw new RuntimeException("Unreachable");
        }
    }

    private Visitable elem(Visitable parameter) {
        System.out.println("Elem " + index);
        if (nextIs('(')) {
            match('(');
            Visitable ret = regExp(null);
            match(')');
            return ret;
        }
        else if (nextIsAlphaNumeric()) {
            return alphanum(null);
        }
        else {
            throwErrorMessage("Expected '(' or char");
            throw new RuntimeException("Unreachable");
        }
    }

    private Visitable alphanum(Visitable parameter) {
        System.out.println("Alphanum " + index);
        if (nextIsAlphaNumeric()) {
            return new OperandNode(""+next());
        }
        else {
            throwErrorMessage("Expected char");
            throw new RuntimeException("Unreachable");
        }
    }
}
