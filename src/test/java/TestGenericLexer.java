import c4_3_transition_matrix_dea.DFAState;
import c4_4_generic_lexer.Lexer;
import org.junit.jupiter.api.*;

import java.util.Map;

import static dea_testing_toolbox.DFATestToolbox.mockStateTransitionTable;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestGenericLexer {
    Map<DFAState, Map<String, DFAState>> stateTransitionTable;

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
        String validMinimalExpression = "x";

        assertFalse(testLexer.match(validMinimalExpression));
    }

    @Test
    @Order(3)
    public void TestValidWithStartRepetition() {
        String validMinimalExpression = "bbbabb";

        assertTrue(testLexer.match(validMinimalExpression));
    }

    @Test
    @Order(4)
    public void TestNonAccepting() {
        String validMinimalExpression = "abbb";

        assertFalse(testLexer.match(validMinimalExpression));
    }

    @Test
    @Order(5)
    public void TestValidMultirepetition() {
        String validMinimalExpression = "bbbbaaaaabaabababbaababbbbbbabb";

        assertTrue(testLexer.match(validMinimalExpression));
    }
}
