package visitor;

import c4_1_syntax_tree.*;

import java.util.Objects;

public class SyntaxTreeEvaluator implements Visitor {

    private int position = 1;

    @Override
    public void visit(OperandNode node) {
        node.nullable = Objects.equals(node.symbol, "epsilon");
        node.position = position++;

        node.firstpos.add(position);
        node.lastpos.add(position);
    }

    @Override
    public void visit(BinOpNode node) {
        SyntaxNode leftNode = (SyntaxNode) node.left;
        SyntaxNode rightNode = (SyntaxNode) node.right;

        switch (node.operator) {
            case "|" -> { // or
                node.nullable = leftNode.nullable || rightNode.nullable;
                node.firstpos.addAll(leftNode.firstpos);
                node.firstpos.addAll(rightNode.firstpos);
                node.lastpos.addAll(leftNode.lastpos);
                node.lastpos.addAll(rightNode.lastpos);
            }
            case "°" -> { // concat
                node.nullable = leftNode.nullable && rightNode.nullable;
                node.firstpos.addAll(leftNode.firstpos);
                if (leftNode.nullable) node.firstpos.addAll(rightNode.firstpos);
                node.lastpos.addAll(rightNode.lastpos);
                if (rightNode.nullable) node.lastpos.addAll(leftNode.lastpos);
            }
            default -> throw new RuntimeException("Binary node has a bad operator!");
        }
    }

    @Override
    public void visit(UnaryOpNode node) {
        SyntaxNode subNode = (SyntaxNode) node.subNode;

        switch (node.operator) {
            case "*", "?" -> { // star, option
                node.nullable = true;
                node.firstpos.addAll(subNode.firstpos);
                node.lastpos.addAll(subNode.lastpos);
            }
            case "+" -> { // plus
                node.nullable = subNode.nullable;
                node.firstpos.addAll(subNode.firstpos);
                node.lastpos.addAll(subNode.lastpos);
            }
            default -> throw new RuntimeException("Unary node has bad operator!");
        }
    }
}
