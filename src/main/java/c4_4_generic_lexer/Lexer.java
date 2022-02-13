// Author: Gabriel Nill
package c4_4_generic_lexer;

import c4_3_transition_matrix_dfa.DFAState;

import java.util.Map;

public class Lexer {
    private final Map<DFAState, Map<Character, DFAState>> stateTransitionTable;

    public Lexer(Map<DFAState, Map<Character, DFAState>> stateTransitionTable) {
        this.stateTransitionTable = stateTransitionTable;
    }

    public boolean match(String word) {
        DFAState state = startPos();
        if (state == null) {
            return false;
        }

        char[] symbols = word.toCharArray();

        for (char symbol : symbols) {
            if (!stateTransitionTable.get(state).containsKey(symbol)) {
                return false;
            }
            state = stateTransitionTable.get(state).get(symbol);
        }
        return state.isAcceptingState;
    }

    private DFAState startPos() {
        try {
            for (DFAState state : stateTransitionTable.keySet()) {
                if (state.index == 1) {
                    return state;
                }
            }
            System.out.println("Lexer error: no starting state recorded.");
        } catch (Exception ex) {
            // that way the result will be zero
            System.out.println("Lexer error: corrupt or null stateTransitionTable");
        }
        return null;
    }
}

