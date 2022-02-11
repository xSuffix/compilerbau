package c4_1_syntax_tree;

public class UnaryOpNode extends SyntaxNode implements Visitable {
    public String operator;
    public Visitable subNode;

    public UnaryOpNode(String operator, Visitable subNode) {
        this.operator = operator;
        this.subNode = subNode;
    }

    @Override
    public String toString() {
        return "(" + subNode.toString() + ")" + operator;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }

        var other = (UnaryOpNode) obj;
        return operator.equals(other.operator)
            && subNode.equals(other.subNode);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
