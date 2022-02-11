import c4_1_syntax_tree.*;

public class Main {
    public static void main(String[] args) {
        var input = "((T|D)e+s(d?)t1*02)#";
        var parser = new Parser(input);
        System.out.println(input);
        System.out.println(parser.start());
    }
}
