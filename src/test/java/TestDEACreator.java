import c4_3_transition_matrix_dea.DFACreator;
import c4_3_transition_matrix_dea.DFAState;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import c4_2_visitor.FollowposTableEntry;

import java.util.*;

import static dea_testing_toolbox.DFATestToolbox.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDEACreator {
    @Test
    @Order(1)
    public void testDFACreator()
    {
        Set<Integer> positionsOfStartState = new HashSet<>(Arrays.asList(1,2,3));

        // hart kodiert über Toolbox-Helperfunction
        SortedMap<Integer, FollowposTableEntry> followposTableEntries = mockFollowposTableEntries();
        Map<DFAState, Map<String, DFAState>> stateTransitionTable = mockStateTransitionTable();

        DFACreator creator = new DFACreator(positionsOfStartState, followposTableEntries);
        creator.populateStateTransitionTable();

        // Ausgabe beider Tabellen (expected, actual) im JSON-Format
        printTables(stateTransitionTable, creator);

        assertEquals(stateTransitionTable, creator.getStateTransitionTable());
    }
}
