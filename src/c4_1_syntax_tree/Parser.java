package c4_1_syntax_tree;

public interface Parser {
    public void initialize(String input);

    public SyntaxNode start();
}
