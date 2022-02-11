package c4_1_syntax_tree;

public class BinOpNode extends SyntaxNode implements Visitable {
    public String operator;
    public Visitable left;
    public Visitable right;

    public BinOpNode(String operator, Visitable left, Visitable right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ")" + operator + "(" + right.toString() + ")";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}