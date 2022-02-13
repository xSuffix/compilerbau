// Author: Jan Fröhlich
package visitor;

import syntax_tree.*;

public class SyntaxTreeEvaluator implements Visitor {

    private int position = 0;

    @Override
    public void visit(OperandNode node) {
        node.position = ++position;

        if (node.symbol != null) {
            node.nullable = false;
            node.firstpos.add(position);
            node.lastpos.add(position);
        } else {
            node.nullable = true;
            node.firstpos.add(null);
            node.lastpos.add(null);
        }
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
