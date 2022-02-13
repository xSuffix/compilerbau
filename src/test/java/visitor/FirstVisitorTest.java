// Author: Jan Fröhlich
package visitor;

import c4_1_syntax_tree.BinOpNode;
import c4_1_syntax_tree.OperandNode;
import c4_1_syntax_tree.UnaryOpNode;
import c4_1_syntax_tree.Visitable;
import c4_2_visitor.DepthFirstIterator;
import c4_2_visitor.SyntaxTreeEvaluator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FirstVisitorTest {

    // From lecture chapter 3, slide 95
    // Regex: (a|b)*cd*
    @Test
    public void testFirstVisitor_01() {
        // Input syntax tree with nullable, firstpos and lastpos
        Visitable syntaxTreeWithValues = StaticSyntaxTree.getTestTree_01();

        // Same syntax tree but values filled by SyntaxTreeEvaluator
        Visitable syntaxTreeByVisitor = new BinOpNode("°", new BinOpNode("°", new BinOpNode("°", new UnaryOpNode("*", new BinOpNode("|", new OperandNode("a"), new OperandNode("b"))), new OperandNode("c")), new UnaryOpNode("*", new OperandNode("d"))), new OperandNode("#"));
        DepthFirstIterator.traverse(syntaxTreeByVisitor, new SyntaxTreeEvaluator());

        assertTrue(equals(syntaxTreeWithValues, syntaxTreeByVisitor));
    }

    // Regex: d*(h|b)+w?
    @Test
    public void testFirstVisitor_02() {
        // Input syntax tree with nullable, firstpos and lastpos
        Visitable syntaxTreeWithValues = StaticSyntaxTree.getTestTree_02();

        // Same syntax tree but values filled by SyntaxTreeEvaluator
        Visitable syntaxTreeByVisitor = new BinOpNode("°", new BinOpNode("°", new BinOpNode("°", new UnaryOpNode("*", new OperandNode("d")), new UnaryOpNode("+", new BinOpNode("|", new OperandNode("h"), new OperandNode("b")))), new UnaryOpNode("?", new OperandNode("w"))), new OperandNode("#"));
        DepthFirstIterator.traverse(syntaxTreeByVisitor, new SyntaxTreeEvaluator());

        assertTrue(equals(syntaxTreeWithValues, syntaxTreeByVisitor));
    }

    private boolean equals(Visitable expected, Visitable visited) {
        if (expected == null && visited == null) return true;
        if (expected == null || visited == null) return false;
        if (expected.getClass() != visited.getClass()) return false;
        if (expected.getClass() == BinOpNode.class) {
            BinOpNode op1 = (BinOpNode) expected;
            BinOpNode op2 = (BinOpNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos) &&
                    equals(op1.left, op2.left) &&
                    equals(op1.right, op2.right);
        }
        if (expected.getClass() == UnaryOpNode.class) {
            UnaryOpNode op1 = (UnaryOpNode) expected;
            UnaryOpNode op2 = (UnaryOpNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos) &&
                    equals(op1.subNode, op2.subNode);
        }
        if (expected.getClass() == OperandNode.class) {
            OperandNode op1 = (OperandNode) expected;
            OperandNode op2 = (OperandNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos);
        }
        throw new IllegalStateException(
                String.format("Beide Wurzelknoten sind Instanzen der Klasse %1$s !" + " Dies ist nicht erlaubt!",
                        expected.getClass().getSimpleName())
        );
    }

}
