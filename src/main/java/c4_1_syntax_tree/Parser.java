package c4_1_syntax_tree;

import java.text.ParseException;

public class Parser {
    private int index;
    private String input;

    public Parser() { }

    public Parser(String input) {
        initialize(input);
    }

    public void initialize(String input) {
        index = 0;
        this.input = input;
    }

    private void throwErrorMessage(String message) throws ParseException {
        System.out.println("SyntaxError!");

        System.out.print(message);
        System.out.print(" at index ");
        System.out.println(index);

        System.out.println(input);

        for (int i = 0; i < index; i++) {
            System.out.print(" ");
        }
        System.out.println("^ here");

        throw new ParseException("SyntaxError!", index);
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

    private boolean isNextAlphaNumeric() {
        if (endOfInput()) {
            return false;
        }

        return Character.isLetterOrDigit(input.charAt(index));
    }

    private void match(char expect) throws ParseException {
        if (!nextIs(expect)) {
            throwErrorMessage("Expected '" + expect + "'");
        }

        index++;
    }

    private void assertEndOfInput() throws ParseException {
        if (!endOfInput()) {
            throwErrorMessage("Expected end of input");
        }
    }

    //
    // Parse Methods:
    //

    public Visitable start() throws ParseException {
        if (nextIs('#')) {
            match('#');
            assertEndOfInput();
            return new OperandNode("#");
        } else if (nextIs('(')) {
            match('(');
            Visitable regexp = regExp(null);
            match(')');
            match('#');
            assertEndOfInput();

            return new BinOpNode("°", regexp, new OperandNode("#"));
        } else {
            throwErrorMessage("Expected # or (");
            return null;
        }
    }

    private Visitable regExp(Visitable parameter) throws ParseException {
        return re(term(null));
    }

    private Visitable re(Visitable parameter) throws ParseException {
        if (nextIs('|')) {
            match('|');

            Visitable term = term(null);
            Visitable result = new BinOpNode("|", parameter, term);

            return re(result);
        } else if (nextIs(')')) {
            return parameter;
        } else {
            throwErrorMessage("Expected '|' or ')'");
            return null;
        }
    }

    private Visitable term(Visitable parameter) throws ParseException {
        if (nextIs('(') || isNextAlphaNumeric()) {
            Visitable factorTree = factor(null);

            if (parameter != null) {
                Visitable root = new BinOpNode("°", parameter, factorTree);
                return term(root);
            }

            return term(factorTree);
        } else if (nextIs('|') || nextIs(')')) {
            return parameter;
        } else {
            throwErrorMessage("Expected '(', char, '|' or ')'");
            return null;
        }
    }

    private Visitable factor(Visitable parameter) throws ParseException {
        if (nextIs('(') || isNextAlphaNumeric()) {
            Visitable ret = elem(null);
            return hOp(ret);
        } else {
            throwErrorMessage("Expected '(' or char");
            return null;
        }
    }

    private Visitable hOp(Visitable parameter) throws ParseException {
        if (nextIs('*')) {
            match('*');
            return new UnaryOpNode("*", parameter);
        } else if (nextIs('+')) {
            match('+');
            return new UnaryOpNode("+", parameter);
        } else if (nextIs('?')) {
            match('?');
            return new UnaryOpNode("?", parameter);
        } else if (nextIs('(') || nextIs('|') || nextIs(')') || isNextAlphaNumeric()) {
            return parameter;
        } else {
            throwErrorMessage("Expected '*', '+', '?', '(', '|', ')' or char");
            return null;
        }
    }

    private Visitable elem(Visitable parameter) throws ParseException {
        if (nextIs('(')) {
            match('(');
            Visitable ret = regExp(null);
            match(')');
            return ret;
        } else if (isNextAlphaNumeric()) {
            return alphanum(null);
        } else {
            throwErrorMessage("Expected '(' or char");
            return null;
        }
    }

    private Visitable alphanum(Visitable parameter) throws ParseException {
        char check = input.charAt(index);
        if (Character.isLetterOrDigit(check)) {
            match(check);
        } else {
            throwErrorMessage("Expected char");
        }

        return new OperandNode(String.valueOf(check));
    }
}
