// Author: Gabriel Nill
package dfa_testing_toolbox;

import c4_2_visitor.FollowposTableEntry;
import c4_3_transition_matrix_dfa.DFAState;

import java.util.*;

public class Utils {
    public static Map<DFAState, Map<Character, DFAState>> mockStateTransitionTable() {
        return Map.ofEntries(
                new AbstractMap.SimpleEntry<>(
                        new DFAState(1, false, new HashSet<>(Arrays.asList(1, 2, 3))),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(
                                        'a',
                                        new DFAState(2, false, new HashSet<>(Arrays.asList(1, 2, 3, 4))
                                        )), new AbstractMap.SimpleEntry<>(
                                        'b',
                                        new DFAState(3, false, new HashSet<>(Arrays.asList(1, 2, 3))
                                        ))
                        )
                ),
                new AbstractMap.SimpleEntry<>(
                        new DFAState(2, false, new HashSet<>(Arrays.asList(1, 2, 3, 4))),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(
                                        'a',
                                        new DFAState(4, false, new HashSet<>(Arrays.asList(1, 2, 3, 4))
                                        )), new AbstractMap.SimpleEntry<>(
                                        'b',
                                        new DFAState(5, false, new HashSet<>(Arrays.asList(1, 2, 3, 5))
                                        ))
                        )
                ),
                new AbstractMap.SimpleEntry<>(
                        new DFAState(5, false, new HashSet<>(Arrays.asList(1, 2, 3, 5))),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(
                                        'a',
                                        new DFAState(6, false, new HashSet<>(Arrays.asList(1, 2, 3, 4))
                                        )), new AbstractMap.SimpleEntry<>(
                                        'b',
                                        new DFAState(7, true, new HashSet<>(Arrays.asList(1, 2, 3, 6))
                                        ))
                        )
                ),
                new AbstractMap.SimpleEntry<>(
                        new DFAState(7, true, new HashSet<>(Arrays.asList(1, 2, 3, 6))),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(
                                        'a',
                                        new DFAState(8, false, new HashSet<>(Arrays.asList(1, 2, 3, 4))
                                        )), new AbstractMap.SimpleEntry<>(
                                        'b',
                                        new DFAState(9, false, new HashSet<>(Arrays.asList(1, 2, 3))
                                        ))
                        )
                )
        );
    }

    public static SortedMap<Integer, FollowposTableEntry> mockFollowPosTableEntries() {
        FollowposTableEntry entry1 = new FollowposTableEntry(1, "a");
        entry1.followpos.addAll(Arrays.asList(1, 2, 3));
        FollowposTableEntry entry2 = new FollowposTableEntry(2, "b");
        entry2.followpos.addAll(Arrays.asList(1, 2, 3));
        FollowposTableEntry entry3 = new FollowposTableEntry(3, "a");
        entry3.followpos.add(4);
        FollowposTableEntry entry4 = new FollowposTableEntry(4, "b");
        entry4.followpos.add(5);
        FollowposTableEntry entry5 = new FollowposTableEntry(5, "b");
        entry5.followpos.add(6);
        FollowposTableEntry entry6 = new FollowposTableEntry(6, "#");
        return new TreeMap<>(Map.ofEntries(
                new AbstractMap.SimpleEntry<>(1, entry1),
                new AbstractMap.SimpleEntry<>(2, entry2),
                new AbstractMap.SimpleEntry<>(3, entry3),
                new AbstractMap.SimpleEntry<>(4, entry4),
                new AbstractMap.SimpleEntry<>(5, entry5),
                new AbstractMap.SimpleEntry<>(6, entry6)
        ));
    }
}
