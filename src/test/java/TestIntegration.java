// Author: Gabriel Nill
// Er hat darauf bestanden. Ein kleiner Bonus :)

import c4_1_syntax_tree.Parser;
import c4_1_syntax_tree.SyntaxNode;
import c4_1_syntax_tree.Visitable;
import c4_2_visitor.DepthFirstIterator;
import c4_2_visitor.FollowPosTableGenerator;
import c4_2_visitor.SyntaxTreeEvaluator;
import c4_3_transition_matrix_dfa.DFACreator;
import c4_4_generic_lexer.Lexer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestIntegration {
    @Test
    @Order(1)
    public void integrationTest() {
        Parser testParser = new Parser("((b|(a(a|ba|bba)*bbb))*abb)#");
        Visitable syntaxTree = testParser.start();
        DepthFirstIterator.traverse(syntaxTree, new SyntaxTreeEvaluator());
        FollowPosTableGenerator fpGenerator = new FollowPosTableGenerator();
        DepthFirstIterator.traverse(syntaxTree, fpGenerator);
        DFACreator dfaCreator = new DFACreator(((SyntaxNode) syntaxTree).firstpos, fpGenerator.getFollowPosTable());
        dfaCreator.populateStateTransitionTable();
        Lexer lexer = new Lexer(dfaCreator.getStateTransitionTable());
        assertTrue(lexer.match("bbabbbaaaaaabbabbbabbabbbabababababbbabb"));
    }

    @Test
    @Order(2)
    public void integrationFailTest() {
        Parser testParser = new Parser("((b|(a(a|ba|bba)*bbb))*abb)#");
        Visitable syntaxTree = testParser.start();
        DepthFirstIterator.traverse(syntaxTree, new SyntaxTreeEvaluator());
        FollowPosTableGenerator fpGenerator = new FollowPosTableGenerator();
        DepthFirstIterator.traverse(syntaxTree, fpGenerator);
        DFACreator dfaCreator = new DFACreator(((SyntaxNode) syntaxTree).firstpos, fpGenerator.getFollowPosTable());
        dfaCreator.populateStateTransitionTable();
        Lexer lexer = new Lexer(dfaCreator.getStateTransitionTable());
        assertFalse(lexer.match("amogus"));
    }
}
