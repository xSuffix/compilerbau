// Author: Gabriel Nill

import visitor.FollowposTableEntry;
import transition_matrix_dfa.DFACreator;
import transition_matrix_dfa.DFAState;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.*;

import static dfa_testing_toolbox.Utils.mockFollowPosTableEntries;
import static dfa_testing_toolbox.Utils.mockStateTransitionTable;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DFACreatorTest {

    @Test
    @Order(1)
    public void testDFACreator() {
        Set<Integer> positionsOfStartState = new HashSet<>(Arrays.asList(1, 2, 3));

        // hardcoded by toolbox-helper-function
        SortedMap<Integer, FollowposTableEntry> followPosTableEntries = mockFollowPosTableEntries();
        Map<DFAState, Map<Character, DFAState>> stateTransitionTable = mockStateTransitionTable();

        DFACreator creator = new DFACreator(positionsOfStartState, followPosTableEntries);
        creator.populateStateTransitionTable();

        assertEquals(stateTransitionTable, creator.getStateTransitionTable());
    }
}
