package c4_3_transition_matrix_dea;

import c4_2_visitor.FollowposTableEntry;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DFACreator {
    private final Set<Integer> positionsForStartState;
    private final SortedMap<Integer, FollowposTableEntry> followposTable;
    private final Map<DFAState, Map<Character, DFAState>> stateTransitionTable;

    /**
     * Man beachte ! Parameter <code>positionsForStartState</code> muss vom Aufrufer
     * mit der firstpos-Menge des Wurzelknotens des Syntaxbaums initialisiert werden !
     */
    public DFACreator(Set<Integer> positionsForStartState, SortedMap<Integer, FollowposTableEntry> followposTable) {
        this.positionsForStartState = positionsForStartState;
        this.followposTable = followposTable;
        this.stateTransitionTable = new HashMap<>();
    }

    public void populateStateTransitionTable() {
        int index = 1;
        Queue<DFAState> qStates = new LinkedList<>();
        DFAState firstPos = new DFAState(index, accepts(positionsForStartState), positionsForStartState);
        qStates.add(firstPos);

        while (!qStates.isEmpty()) {
            DFAState currentState = qStates.remove();
            stateTransitionTable.put(currentState, new HashMap<>());

            for (char symbol : inputAlphabet()) {
                DFAState followingState = followingState(++index, currentState, symbol);
                stateTransitionTable.get(currentState).put(symbol, followingState);
                if (!stateTransitionTable.containsKey(followingState) && !qStates.contains(followingState)) {
                    qStates.add(followingState);
                }
            }
        }
    }

    public Map<DFAState, Map<Character, DFAState>> getStateTransitionTable() {
        return stateTransitionTable;
    }

    private Set<Character> inputAlphabet() {
        Set<Character> alphabet = new HashSet<>();
        for (FollowposTableEntry entry : followposTable.values()) {
            if (entry.symbol.toCharArray()[0] != '#') {
                alphabet.add(entry.symbol.toCharArray()[0]);
            }
        }
        return alphabet;
    }

    private boolean accepts(Set<Integer> positionsSet) {
        for (Integer position : positionsSet) {
            for (FollowposTableEntry entry : followposTable.values()) {
                if (position == entry.position && entry.followpos.contains(followposTable.size())) {
                    return true;
                }
            }
        }
        return false;
    }

    private DFAState followingState(Integer index, DFAState currentState, char symbol) {
        Set<Integer> followingPositions = new HashSet<>();
        for (Integer position : currentState.positionsSet) {
            for (FollowposTableEntry entry : followposTable.values()) {
                if (position == entry.position && symbol == entry.symbol.toCharArray()[0]) {
                    followingPositions = Stream.of(followingPositions, entry.followpos)
                            .flatMap(Set::stream)
                            .collect(Collectors.toSet());
                }
            }
        }
        return new DFAState(index, accepts(followingPositions), followingPositions);
    }
}
