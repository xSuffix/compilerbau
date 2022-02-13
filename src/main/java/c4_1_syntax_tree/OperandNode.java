package c4_1_syntax_tree;

public class OperandNode extends SyntaxNode implements Visitable {
    public int position;
    public String symbol;

    public OperandNode(String symbol) {
        position = -1; // not initialized yet
        this.symbol = symbol;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
