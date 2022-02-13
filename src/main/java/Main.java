import c4_1_syntax_tree.Parser;
import c4_1_syntax_tree.SyntaxNode;
import c4_1_syntax_tree.Visitable;
import c4_2_visitor.DepthFirstIterator;
import c4_2_visitor.FollowPosTableGenerator;
import c4_2_visitor.SyntaxTreeEvaluator;
import c4_3_transition_matrix_dfa.DFACreator;
import c4_4_generic_lexer.Lexer;

public class Main {
    public static void main(String[] args) {
        Parser testParser = new Parser("(abc)#");
        Visitable syntaxTree = testParser.start();
        DepthFirstIterator.traverse(syntaxTree, new SyntaxTreeEvaluator());
        FollowPosTableGenerator fpGenerator = new FollowPosTableGenerator();
        DepthFirstIterator.traverse(syntaxTree, fpGenerator);
        DFACreator dfaCreator = new DFACreator(((SyntaxNode)syntaxTree).firstpos, fpGenerator.getFollowPosTable());
        dfaCreator.populateStateTransitionTable();
        Lexer lexer = new Lexer(dfaCreator.getStateTransitionTable());
        System.out.println(lexer.match("abc") ? "DFA accepts the word." : "DFA does not accept the word.");
    }
}
