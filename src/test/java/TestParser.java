import c4_1_syntax_tree.*;
import c4_2_visitor.*;
import c4_3_transition_matrix_dea.*;
import c4_4_generic_lexer.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestParser {
    Parser parser;

    @BeforeEach
    public void init() {
        parser = new Parser();
    }

    @Test
    public void testEmpty() {
        parser.initialize("()#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new OperandNode(""), new OperandNode("#"));

        equals(expected, actual);
    }

    @Test
    public void testSimple1() {
        parser.initialize("(a)#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new OperandNode("a"), new OperandNode("#"));

        assertEquals(expected, actual);
    }

    @Test
    public void testSimple2() {
        parser.initialize("(ab)#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new BinOpNode("°", new OperandNode("a"), new OperandNode("b")), new OperandNode("#"));

        assertEquals(expected, actual);
    }

    @Test
    public void testSimple3() {
        parser.initialize("(a|b)#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new BinOpNode("|", new OperandNode("a"), new OperandNode("b")), new OperandNode("#"));

        assertEquals(expected, actual);
    }

    @Test
    public void testSimple4() {
        parser.initialize("((a))#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new OperandNode("a"), new OperandNode("#"));

        assertEquals(expected, actual);
    }

    @Test
    public void testSimple5() {
        parser.initialize("(a*)#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new UnaryOpNode("*", new OperandNode("a")), new OperandNode("#"));

        assertEquals(expected, actual);
    }

    @Test
    public void testSimple6() {
        parser.initialize("((a*)?)#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new UnaryOpNode("?", new UnaryOpNode("*", new OperandNode("a"))), new OperandNode("#"));

        assertEquals(expected, actual);
    }

    @Test
    public void testSimple7() {
        parser.initialize("(a|)#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new BinOpNode("|", new OperandNode("a"), new OperandNode("")), new OperandNode("#"));

        assertEquals(expected, actual);
    }

    @Test
    public void testSimple8() {
        parser.initialize("(|b)#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new BinOpNode("|", new OperandNode(""), new OperandNode("b")), new OperandNode("#"));

        assertEquals(expected, actual);
    }

    @Test
    public void testSimple9() {
        parser.initialize("(|)#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new BinOpNode("|", new OperandNode(""), new OperandNode("")), new OperandNode("#"));

        assertEquals(expected, actual);
    }

    @Test
    public void testComplex0() {
        parser.initialize("(ab*c|(a+)?)#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new BinOpNode("|", new BinOpNode("°", new OperandNode("a"), new BinOpNode("°", new UnaryOpNode("*", new OperandNode("b")), new OperandNode("c"))), new UnaryOpNode("?", new UnaryOpNode("+", new OperandNode("a")))), new OperandNode("#"));

        assertEquals(expected, actual);
    }

    @Test
    public void testComplex1() {
        parser.initialize("(((a|c)*b+)*d*|(d(e|)?)f)#");
        var actual = parser.start();
        var expected = new BinOpNode("°", new BinOpNode("|", new BinOpNode("°", new UnaryOpNode("*", new BinOpNode("°", new UnaryOpNode("*", new BinOpNode("|", new OperandNode("a"), new OperandNode("c"))), new UnaryOpNode("+", new OperandNode("b")))), new UnaryOpNode("*", new OperandNode("d"))), new BinOpNode("°", new BinOpNode("°", new OperandNode("d"), new UnaryOpNode("?", new BinOpNode("|", new OperandNode("e"), new OperandNode("")))), new OperandNode("f"))), new OperandNode("#"));

        assertEquals(expected, actual);
    }

    private static boolean equals(Visitable v1, Visitable v2)
    {
        if (v1 == v2)
            return true;
        if (v1 == null)
            return false;
        if (v2 == null)
            return false;
        if (v1.getClass() != v2.getClass())
            return false;
        if (v1.getClass() == OperandNode.class)
        {
            OperandNode op1 = (OperandNode) v1;
            OperandNode op2 = (OperandNode) v2;
            return op1.position == op2.position && op1.symbol.equals(op2.symbol);
        }
        if (v1.getClass() == UnaryOpNode.class)
        {
            UnaryOpNode op1 = (UnaryOpNode) v1;
            UnaryOpNode op2 = (UnaryOpNode) v2;
            return op1.operator.equals(op2.operator) && equals(op1.subNode,
                    op2.subNode);
        }
        if (v1.getClass() == BinOpNode.class) {
            BinOpNode op1 = (BinOpNode) v1;
            BinOpNode op2 = (BinOpNode) v2;
            return op1.operator.equals(op2.operator) &&
                    equals(op1.left, op2.left) &&
                    equals(op1.right, op2.right);
        }
        throw new IllegalStateException("Ungueltiger Knotentyp");

    }
}
