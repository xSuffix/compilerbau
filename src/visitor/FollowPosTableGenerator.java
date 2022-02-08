package visitor;

import c4_1_syntax_tree.*;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class FollowPosTableGenerator implements Visitor {

    private final SortedMap<Integer, FollowposTableEntry> followPosTableEntries = new TreeMap<>();

    public SortedMap<Integer, FollowposTableEntry> getFollowPosTable() {
        return followPosTableEntries;
    }

    @Override
    public void visit(OperandNode node) {
        // Default in followpos-Table
        followPosTableEntries.put(
                node.position,
                new FollowposTableEntry(node.position, node.symbol));
    }

    @Override
    public void visit(BinOpNode node) {
        // Only | and ° are possible
        if (node.operator.equals("°")) {
            Set<Integer> lastpos;
            Set<Integer> firstpos;
            try {
                // lastpos from node.left
                lastpos = ((SyntaxNode) node.left).lastpos;
                // firstpos from node.right
                firstpos = ((SyntaxNode) node.right).firstpos;
            } catch (Exception e) {
                throw new RuntimeException("Node is not a SyntaxNode.");
            }

            // Algorithm
            for (int i : lastpos) {
                followPosTableEntries.get(i).followpos.addAll(firstpos);
            }
        }
        // Ignore |
    }

    @Override
    public void visit(UnaryOpNode node) {
        // Only *, + and ? are possible
        if (node.operator.equals("?")) {
            return; // Ignore
        }

        Set<Integer> lastpos;
        Set<Integer> firstpos;

        try {
            // lastpos from node.subNode
            lastpos = ((SyntaxNode) node.subNode).lastpos;
            // firstpos from node.subNode
            firstpos = ((SyntaxNode) node.subNode).firstpos;
        } catch (Exception e) {
            throw new RuntimeException("Sub node is not a SyntaxNode.");
        }

        if (node.operator.equals("*") || node.operator.equals("+")) {
            // Algorithm
            for (int i : lastpos) {
                followPosTableEntries.get(i).followpos.addAll(firstpos);
            }
        }
    }
}
