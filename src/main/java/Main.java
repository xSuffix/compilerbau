import c4_1_syntax_tree.*;

public class Main {
    public static void main(String[] args) {
        var input = "(a|)#";
        var parser = new Parser(input);
        System.out.println(input);
        System.out.println(parser.start());
    }
}
