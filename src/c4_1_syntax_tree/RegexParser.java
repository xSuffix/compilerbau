package c4_1_syntax_tree;

public class RegexParser implements Parser {
    int index;
    String input;

    public RegexParser() {
    }
    public RegexParser(String input) {
        initialize(input);
    }

    public void initialize(String input) {
        index = 0;
        this.input = input;
    }

    void throwErrorMessage(String message) {
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

    boolean endOfInput() {
        return index >= input.length();
    }

    boolean nextIs(char peek) {
        if (endOfInput()) {
            return false;
        }

        return input[index] == peek;
    }

    boolean nextIsAlphaNumeric() {
        for (int i = 0; i < 26; i++) {
            if (nextIs((char)('A' + i))) {
                return true;
            }
            if (nextIs((char)('a' + i))) {
                return true;
            }
        }

        for (int i = 0; i < 10; i++) {
            if (nextIs((char)('0' + i))) {
                return true;
            }
        }

        return false;
    }

    char next() {
        if (endOfInput()) {
            throwErrorMessage("Unexpected end of input");
        }

        return input[index++];
    }

    void match(char expect) {
        if (!nextIs(expect)) {
            throwErrorMessage("Expected '" + expect + "'");
        }
        next();
    }

    void matchEndOfInput() {
        if (!endOfInput()) {
            throwErrorMessage("Expected end of input");
        }
    }

    //
    // Parse Methods:
    //

    // start -> regex '#'
    public SyntaxNode start() {
        var result = regex();
        matchEndOfInput();

        return result;
    }

    // regex -> regex_left regex_right
    SyntaxNode regex() {
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
    SyntaxNode regexLeft() {
        if (nextIs('(') || nextIsAlphaNumeric()) {
            var item = elementAndOperator();
            var next = regexLeft();

            if (next != null) {
                return new BinOpNode("°", item, next);
            }

            return item;
        }
    }

    // element_and_operator -> element operator
    SyntaxNode elementAndOperator() {
        var el = element();
        var op = operator();

        if (op == 0) {
            return el;
        }

        new UnaryOpNode(""+op, el);
    }

    // regex_right -> ''
    // regex_right -> '|' regex
    SyntaxNode regexRight() {
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
    SyntaxNode element() {
        if (nextIs('(')) {
            match('(');
            var re = regex();
            match(')');

            return re;
        }

        return new OperandNode(alphaNumeric());
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
    }
}
