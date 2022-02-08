package c4_3_transition_matrix_dea;

import java.util.HashMap;
import java.util.Map;

public class DFA<Char> {
    // Klasse wird nicht benötigt, nur:
    private final Map<DFAState, Map<Char, DFAState>> stateTransitionTable = new HashMap<>();
}
