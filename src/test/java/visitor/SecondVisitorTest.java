// Autor: Fabian Weller
package visitor;

import c4_1_syntax_tree.*;
import c4_2_visitor.DepthFirstIterator;
import c4_2_visitor.FollowPosTableGenerator;
import c4_2_visitor.FollowposTableEntry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class SecondVisitorTest {

    // From lecture chapter 3, slide 94ff
    // Regex: (a|b)*cd*
    @Test
    public void testSecondVisitor_01() {
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

        // create expected followposTable
        FollowposTableEntry row1 = new FollowposTableEntry(1, "a");
        row1.followpos.addAll(Set.of(1, 2, 3));

        FollowposTableEntry row2 = new FollowposTableEntry(2, "b");
        row2.followpos.addAll(Set.of(1, 2, 3));

        FollowposTableEntry row3 = new FollowposTableEntry(3, "c");
        row3.followpos.addAll(Set.of(4, 5));

        FollowposTableEntry row4 = new FollowposTableEntry(4, "d");
        row4.followpos.addAll(Set.of(4, 5));

        FollowposTableEntry row5 = new FollowposTableEntry(5, "#");

        // add rows to SortedMap
        List<FollowposTableEntry> list = List.of(row1, row2, row3, row4, row5);

        SortedMap<Integer, FollowposTableEntry> expectedTableEntries = new TreeMap<>();

        for (FollowposTableEntry i : list) {
            expectedTableEntries.put(i.position, i);
        }

        FollowPosTableGenerator generator = new FollowPosTableGenerator();
        DepthFirstIterator.traverse(syntaxTreeWithValues, generator);

        assertEquals(
                expectedTableEntries,
                generator.getFollowPosTable()
        );
    }

    // Regex: d*(h|b)+w?
    @Test
    public void testSecondVisitor_02() {
        // Input Syntax tree with nullable, firstpos and lastpos
        OperandNode leafLeft;
        OperandNode leafRight;
        UnaryOpNode kleenschNode;
        UnaryOpNode positiveNode;
        UnaryOpNode optionNode;
        BinOpNode concatenationNode;
        BinOpNode alternativeNode;

        leafLeft = new OperandNode("d");
        leafLeft.position = 1;
        leafLeft.nullable = false;
        leafLeft.firstpos.add(1);
        leafLeft.lastpos.add(1);

        kleenschNode = new UnaryOpNode("*", leafLeft);
        kleenschNode.nullable = true;
        kleenschNode.firstpos.add(1);
        kleenschNode.lastpos.add(1);

        leafLeft = new OperandNode("h");
        leafLeft.position = 2;
        leafLeft.nullable = false;
        leafLeft.firstpos.add(2);
        leafLeft.lastpos.add(2);

        leafRight = new OperandNode("b");
        leafRight.position = 3;
        leafRight.nullable = false;
        leafRight.firstpos.add(3);
        leafRight.lastpos.add(3);

        alternativeNode = new BinOpNode("|", leafLeft, leafRight);
        alternativeNode.nullable = false;
        alternativeNode.firstpos.addAll(Set.of(2, 3));
        alternativeNode.lastpos.addAll(Set.of(2, 3));

        positiveNode = new UnaryOpNode("+", alternativeNode);
        positiveNode.nullable = false;
        positiveNode.firstpos.addAll(Set.of(2, 3));
        positiveNode.lastpos.addAll(Set.of(2, 3));

        concatenationNode = new BinOpNode("°", kleenschNode, positiveNode);
        concatenationNode.nullable = false;
        concatenationNode.firstpos.addAll(Set.of(1, 2, 3));
        concatenationNode.lastpos.addAll(Set.of(2, 3));

        leafRight = new OperandNode("w");
        leafRight.position = 4;
        leafRight.nullable = false;
        leafRight.firstpos.add(4);
        leafRight.lastpos.add(4);

        optionNode = new UnaryOpNode("?", leafRight);
        optionNode.nullable = true;
        optionNode.firstpos.add(4);
        optionNode.lastpos.add(4);

        concatenationNode = new BinOpNode("°", concatenationNode, optionNode);
        concatenationNode.nullable = false;
        concatenationNode.firstpos.addAll(Set.of(1, 2, 3));
        concatenationNode.lastpos.addAll(Set.of(2, 3, 4));

        leafRight = new OperandNode("#");
        leafRight.position = 5;
        leafRight.nullable = false;
        leafRight.firstpos.add(5);
        leafRight.lastpos.add(5);

        BinOpNode syntaxTreeWithValues = new BinOpNode("°", concatenationNode, leafRight);
        syntaxTreeWithValues.nullable = false;
        syntaxTreeWithValues.firstpos.addAll(Set.of(1, 2, 3));
        syntaxTreeWithValues.lastpos.add(5);

        // create expected followposTable
        FollowposTableEntry row1 = new FollowposTableEntry(1, "d");
        row1.followpos.addAll(Set.of(1, 2, 3));

        FollowposTableEntry row2 = new FollowposTableEntry(2, "h");
        row2.followpos.addAll(Set.of(2, 3, 4, 5));

        FollowposTableEntry row3 = new FollowposTableEntry(3, "b");
        row3.followpos.addAll(Set.of(2, 3, 4, 5));

        FollowposTableEntry row4 = new FollowposTableEntry(4, "w");
        row4.followpos.addAll(Set.of(5));

        FollowposTableEntry row5 = new FollowposTableEntry(5, "#");

        // add rows to SortedMap
        List<FollowposTableEntry> list = List.of(row1, row2, row3, row4, row5);

        SortedMap<Integer, FollowposTableEntry> expectedTableEntries = new TreeMap<>();

        for (FollowposTableEntry i : list) {
            expectedTableEntries.put(i.position, i);
        }

        FollowPosTableGenerator generator = new FollowPosTableGenerator();
        DepthFirstIterator.traverse(syntaxTreeWithValues, generator);

        assertEquals(
                expectedTableEntries,
                generator.getFollowPosTable()
        );
    }

}