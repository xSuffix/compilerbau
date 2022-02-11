package c4_4_generic_lexer;

import c4_3_transition_matrix_dea.DFAState;

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

    //  System.out.println("Starting state: " + state.index);
    //  System.out.println("Word: " + word);

        char[] symbols = word.toCharArray();

        for (char letter : symbols) {
            if (!stateTransitionTable.get(state).containsKey(letter)) {
            //  System.out.println("Read symbol: '" + letter + "' -> invalid transition");
                return false;
            }
            state = stateTransitionTable.get(state).get(letter);
        //  System.out.println("Read symbol: '" + letter + "' -> new state: " + state.index);
        }
    //  System.out.println(state.isAcceptingState ? "Accepted" : "Not Accepted");
        return state.isAcceptingState;
    }

    private DFAState startPos() {
        for (DFAState state : stateTransitionTable.keySet()) {
            if (state.index == 1) {
                return state;
            }
        }
        return null;
    }
}

