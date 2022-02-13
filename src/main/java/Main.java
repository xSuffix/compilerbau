import syntax_tree.Parser;
import syntax_tree.SyntaxNode;
import syntax_tree.Visitable;
import visitor.DepthFirstIterator;
import visitor.FollowPosTableGenerator;
import visitor.SyntaxTreeEvaluator;
import transition_matrix_dfa.DFACreator;
import generic_lexer.Lexer;

public class Main {
    public static void main(String[] args) {
        Parser testParser = new Parser("(abc)#");
        Visitable syntaxTree = testParser.start();
        DepthFirstIterator.traverse(syntaxTree, new SyntaxTreeEvaluator());
        FollowPosTableGenerator fpGenerator = new FollowPosTableGenerator();
        DepthFirstIterator.traverse(syntaxTree, fpGenerator);
        DFACreator dfaCreator = new DFACreator(((SyntaxNode) syntaxTree).firstpos, fpGenerator.getFollowPosTable());
        dfaCreator.populateStateTransitionTable();
        Lexer lexer = new Lexer(dfaCreator.getStateTransitionTable());
        System.out.println(lexer.match("abc") ? "DFA accepts the word." : "DFA does not accept the word.");

    }
}
