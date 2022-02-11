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

        assertEquals(expected, actual);
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
}
