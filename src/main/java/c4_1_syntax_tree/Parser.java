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
        else {
            match('(');
            Visitable end = new OperandNode("#");
            Visitable regexp = RegExp(null);
            Visitable root = new BinOpNode("°", regexp, end);

            match(')');
            match('#');
            assertEndOfInput();

            return root;
        }
    }

    private Visitable RegExp(Visitable parameter) {
        System.out.println("RegExp " + index);
        return Re(Term(null));
    }

    private Visitable Re(Visitable parameter) {
        System.out.println("Re " + index);
        if (nextIs('|')) {
            match('|');

            Visitable term = Term(null);
            Visitable result = new BinOpNode("|", parameter, term);

            return Re(result);
        }
        else if (nextIs(')')) {
            return parameter;
        }
        else {
            throwErrorMessage("Expected '|' or ')'");
            throw new RuntimeException("Unreachable");
        }
    }

    private Visitable Term(Visitable parameter) {
        System.out.println("Term " + index);
        if (nextIs('(') || nextIsAlphaNumeric()) {
            Visitable ret = Factor(null);
            Visitable term = null;

            if (parameter != null) {
                term = new BinOpNode("°", parameter, ret);
            }
            else {
                term = ret;
            }

            return Term(term);
        }
        else if (nextIs('|') || nextIs(')')) {
            return parameter;
        }
        else {
            throwErrorMessage("Expected '(', char, '|' or ')'");
            throw new RuntimeException("Unreachable");
        }
    }

    private Visitable Factor(Visitable parameter) {
        System.out.println("Factor " + index);
        if (nextIs('(') || nextIsAlphaNumeric()) {
            Visitable ret = Elem(null);
            return HOp(ret);
        }
        else {
            throwErrorMessage("Expected '(' or char");
            throw new RuntimeException("Unreachable");
        }
    }

    private Visitable HOp(Visitable parameter) {
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

    private Visitable Elem(Visitable parameter) {
        System.out.println("Elem " + index);
        if (nextIs('(')) {
            match('(');
            Visitable ret = RegExp(null);
            match(')');
            return ret;
        }
        else if (nextIsAlphaNumeric()) {
            return Alphanum(null);
        }
        else {
            throwErrorMessage("Expected '(' or char");
            throw new RuntimeException("Unreachable");
        }
    }

    private Visitable Alphanum(Visitable parameter) {
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
