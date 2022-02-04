package c4_3_transition_matrix_dea;

import java.util.HashMap;
import java.util.Map;

public class DFA<Char> {
    private Map<DFAState, Map<Char, DFAState>> stateTransitionTable = new HashMap<>();
}
