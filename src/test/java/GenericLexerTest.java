// Author: Gabriel Nill

import transition_matrix_dfa.DFAState;
import generic_lexer.Lexer;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static dfa_testing_toolbox.Utils.mockStateTransitionTable;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GenericLexerTest {
    Map<DFAState, Map<Character, DFAState>> stateTransitionTable;

    Lexer testLexer;

    @BeforeEach
    public void init() {
        stateTransitionTable = mockStateTransitionTable();
        testLexer = new Lexer(stateTransitionTable);
    }

    @Test
    @Order(1)
    public void TestValidMinimal() {
        String validMinimalExpression = "abb";

        assertTrue(testLexer.match(validMinimalExpression));
    }

    @Test
    @Order(2)
    public void TestInvalidMinimal() {
        String invalidExpression = "x";

        assertFalse(testLexer.match(invalidExpression));
    }

    @Test
    @Order(3)
    public void TestValidWithStartRepetition() {
        String validWithStartRepetition = "bbbabb";

        assertTrue(testLexer.match(validWithStartRepetition));
    }

    @Test
    @Order(4)
    public void TestNonAccepting() {
        String nonAcceptingExpression = "abbb";

        assertFalse(testLexer.match(nonAcceptingExpression));
    }

    @Test
    @Order(5)
    public void TestValidMultiRepetition() {
        String validMultiRepetitionExpression = "bbbbaaaaabaabababbaababbbbbbabb";

        assertTrue(testLexer.match(validMultiRepetitionExpression));
    }

    @Test
    @Order(6)
    public void EmptyAutomaton() {
        Lexer invalidLexer = new Lexer(null);
        String irrelevantExpression = "does not matter";

        assertFalse(invalidLexer.match(irrelevantExpression));
    }

    @Test
    @Order(6)
    public void EmptyAutomatonMap() {
        Lexer invalidLexer = new Lexer(new HashMap<>());
        String irrelevantExpression = "also does not matter";

        assertFalse(invalidLexer.match(irrelevantExpression));
    }
}
