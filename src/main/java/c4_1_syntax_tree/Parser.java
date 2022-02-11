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
        System.out.println("Error!");

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
            throwErrorMessage("Expected '" + expect + "'");
        }
        next();
    }

    private void matchEndOfInput() {
        if (!endOfInput()) {
            throwErrorMessage("Expected end of input");
        }
    }

    //
    // Parse Methods:
    //

    // start -> '(' regex ')' '#'
    public Visitable start() {
        if (nextIs('(')) {
            match('(');
            var result = regex();
            match(')');
            match('#');
            matchEndOfInput();

            return new BinOpNode("°", result, new OperandNode("#"));
        }

        return new OperandNode("#");
    }

    // regex -> regex_left regex_right
    private Visitable regex() {
        var left = regexLeft();
        var right = regexRight();

        if (left == null && right == null) {
            // case ""
            return new OperandNode(""); // empty word
        }

        if (left != null && right != null) {
            // case "a|b"
            return new BinOpNode("|", left, right);
        }

        if (right != null) {
            // case "|b"
            return new BinOpNode("|", new OperandNode(""), right);
        }

        // case "a" or
        // case "a|"
        return left;
    }

    // regex_left -> element_and_operator regex_left
    // regex_left -> ''
    private Visitable regexLeft() {
        if (nextIs('(') || nextIsAlphaNumeric()) {
            var item = elementAndOperator();
            var next = regexLeft();

            if (next != null) {
                return new BinOpNode("°", item, next);
            }

            return item;
        }

        return null;
    }

    // element_and_operator -> element operator
    private Visitable elementAndOperator() {
        var el = element();
        var op = operator();

        if (op == 0) {
            return el;
        }

        return new UnaryOpNode(""+op, el);
    }

    // regex_right -> ''
    // regex_right -> '|' regex
    private Visitable regexRight() {
        if (nextIs('|')) {
            next();
            return regex();
        }

        return null;
    }

    // operator -> ''
    // operator -> '*'
    // operator -> '+'
    // operator -> '?'
    char operator() {
        if (nextIs('*') || nextIs('+') || nextIs('?')) {
            return next();
        }

        return 0;
    }

    // element -> alpha_numeric
    // element -> '(' regex ')'
    private Visitable element() {
        if (nextIs('(')) {
            match('(');
            var re = regex();
            match(')');

            return re;
        }

        return new OperandNode(""+alphaNumeric());
    }


    // alpha_numeric -> 'a'
    // alpha_numeric -> 'b'
    // alpha_numeric -> 'c'
    // ...
    char alphaNumeric() {
        if (nextIsAlphaNumeric()) {
            return next();
        }

        throwErrorMessage("Expected alpha-numeric character");
        return 0;
    }
}
