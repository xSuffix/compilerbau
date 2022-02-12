package visitor;

import c4_1_syntax_tree.*;
import c4_2_visitor.DepthFirstIterator;
import c4_2_visitor.SyntaxTreeEvaluator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FirstVisitorTest {

    // From lecture chapter 3, slide 95
    @Test
    public void testFirstVisitor_01() {
        // Input Syntax tree with nullable, firstpos and lastpos
        OperandNode leafLeft;
        OperandNode leafRight;
        UnaryOpNode kleenschNode;
        BinOpNode concatenationNode;
        BinOpNode alternativeNode;

        leafLeft = new OperandNode("a");
        leafLeft.position = 1;
        leafLeft.nullable = false;
        leafLeft.firstpos.add(1);
        leafLeft.lastpos.add(1);

        leafRight = new OperandNode("b");
        leafRight.position = 2;
        leafRight.nullable = false;
        leafRight.firstpos.add(2);
        leafRight.lastpos.add(2);

        alternativeNode = new BinOpNode("|", leafLeft, leafRight);
        alternativeNode.nullable = false;
        alternativeNode.firstpos.addAll(Set.of(1, 2));
        alternativeNode.lastpos.addAll(Set.of(1, 2));

        kleenschNode = new UnaryOpNode("*", alternativeNode);
        kleenschNode.nullable = true;
        kleenschNode.firstpos.addAll(Set.of(1, 2));
        kleenschNode.lastpos.addAll(Set.of(1, 2));

        leafRight = new OperandNode("c");
        leafRight.position = 3;
        leafRight.nullable = false;
        leafRight.firstpos.add(3);
        leafRight.lastpos.add(3);

        concatenationNode = new BinOpNode("°", kleenschNode, leafRight);
        concatenationNode.nullable = false;
        concatenationNode.firstpos.addAll(Set.of(1, 2, 3));
        concatenationNode.lastpos.add(3);

        leafRight = new OperandNode("d");
        leafRight.position = 4;
        leafRight.nullable = false;
        leafRight.firstpos.add(4);
        leafRight.lastpos.add(4);

        kleenschNode = new UnaryOpNode("*", leafRight);
        kleenschNode.nullable = true;
        kleenschNode.firstpos.add(4);
        kleenschNode.lastpos.add(4);

        concatenationNode = new BinOpNode("°", concatenationNode, kleenschNode);
        concatenationNode.nullable = false;
        concatenationNode.firstpos.addAll(Set.of(1, 2, 3));
        concatenationNode.lastpos.addAll(Set.of(3, 4));

        leafRight = new OperandNode("#");
        leafRight.position = 5;
        leafRight.nullable = false;
        leafRight.firstpos.add(5);
        leafRight.lastpos.add(5);

        BinOpNode syntaxTreeWithValues = new BinOpNode("°", concatenationNode, leafRight);
        syntaxTreeWithValues.nullable = false;
        syntaxTreeWithValues.firstpos.addAll(Set.of(1, 2, 3));
        syntaxTreeWithValues.lastpos.add(5);

        Parser parser = new Parser("((a|b)*cd*)#");
        Visitable syntaxTreeByVisitor = parser.start();
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
