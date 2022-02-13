// Autor: Fabian Weller
package visitor;

import c4_1_syntax_tree.Visitable;
import c4_2_visitor.DepthFirstIterator;
import c4_2_visitor.FollowPosTableGenerator;
import c4_2_visitor.FollowposTableEntry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecondVisitorTest {

    // From lecture chapter 3, slide 94ff
    // Regex: ((a|b)*cd*)#
    @Test
    public void testSecondVisitor_01() {
        // Input Syntax tree with nullable, firstpos and lastpos
        Visitable syntaxTreeWithValues = StaticSyntaxTree.getTestTree_01();

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

    // Regex: (d*(h|b)+w?)#
    @Test
    public void testSecondVisitor_02() {
        // Input Syntax tree with nullable, firstpos and lastpos
        Visitable syntaxTreeWithValues = StaticSyntaxTree.getTestTree_02();

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