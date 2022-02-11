package c4_1_syntax_tree;

public class OperandNode extends SyntaxNode implements Visitable {
    public int position;
    public String symbol;

    public OperandNode(String symbol) {
        position = -1; // bedeutet: noch nicht initialisiert
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public  boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }

        var other = (OperandNode) obj;
        return symbol.equals(other.symbol);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
