package dea_testing_toolbox;

import c4_3_transition_matrix_dea.DFACreator;
import c4_3_transition_matrix_dea.DFAState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import c4_2_visitor.FollowposTableEntry;

import java.lang.reflect.Type;
import java.util.*;

public class DFATestToolbox {
    public static Map<DFAState, Map<Character, DFAState>> mockStateTransitionTable() {
        return Map.ofEntries(
                new AbstractMap.SimpleEntry<>(
                        new DFAState(1, false, new HashSet<>(Arrays.asList(1,2,3))),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(
                                        'a',
                                        new DFAState(2, false, new HashSet<>(Arrays.asList(1,2,3,4))
                                        )),     new AbstractMap.SimpleEntry<>(
                                        'b',
                                        new DFAState(3, false, new HashSet<>(Arrays.asList(1,2,3))
                                        ))
                        )
                ),
                new AbstractMap.SimpleEntry<>(
                        new DFAState(2, false, new HashSet<>(Arrays.asList(1,2,3,4))),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(
                                        'a',
                                        new DFAState(4, false, new HashSet<>(Arrays.asList(1,2,3,4))
                                        )),     new AbstractMap.SimpleEntry<>(
                                        'b',
                                        new DFAState(5, false, new HashSet<>(Arrays.asList(1,2,3,5))
                                        ))
                        )
                ),
                new AbstractMap.SimpleEntry<>(
                        new DFAState(5, false, new HashSet<>(Arrays.asList(1,2,3,5))),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(
                                        'a',
                                        new DFAState(6, false, new HashSet<>(Arrays.asList(1,2,3,4))
                                        )),     new AbstractMap.SimpleEntry<>(
                                        'b',
                                        new DFAState(7, true, new HashSet<>(Arrays.asList(1,2,3,6))
                                        ))
                        )
                ),
                new AbstractMap.SimpleEntry<>(
                        new DFAState(7, true, new HashSet<>(Arrays.asList(1,2,3,6))),
                        Map.ofEntries(
                                new AbstractMap.SimpleEntry<>(
                                        'a',
                                        new DFAState(8, false, new HashSet<>(Arrays.asList(1,2,3,4))
                                        )),     new AbstractMap.SimpleEntry<>(
                                        'b',
                                        new DFAState(9, false, new HashSet<>(Arrays.asList(1,2,3))
                                        ))
                        )
                )
        );
    }
    public static SortedMap<Integer, FollowposTableEntry> mockFollowposTableEntries() {
        FollowposTableEntry a = new FollowposTableEntry(1, "a");
        a.followpos.addAll(Arrays.asList(1,2,3));
        FollowposTableEntry b = new FollowposTableEntry(2, "b");
        b.followpos.addAll(Arrays.asList(1,2,3));
        FollowposTableEntry c = new FollowposTableEntry(3, "a");
        c.followpos.add(4);
        FollowposTableEntry d = new FollowposTableEntry(4, "b");
        d.followpos.add(5);
        FollowposTableEntry e = new FollowposTableEntry(5, "b");
        e.followpos.add(6);
        FollowposTableEntry f = new FollowposTableEntry(6, "#");
        return new TreeMap<>(Map.ofEntries(
                new AbstractMap.SimpleEntry<>(1, a),
                new AbstractMap.SimpleEntry<>(2, b),
                new AbstractMap.SimpleEntry<>(3, c),
                new AbstractMap.SimpleEntry<>(4, d),
                new AbstractMap.SimpleEntry<>(5, e),
                new AbstractMap.SimpleEntry<>(6, f)
        ));
    }

    public static void printTables(Map<DFAState, Map<Character, DFAState>> stateTransitionTable, DFACreator creator) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.enableComplexMapKeySerialization().create();
        Type type = new TypeToken<Map<DFAState, Map<Character, DFAState>>>(){}.getType();
        String json = gson.toJson(stateTransitionTable, type);
        System.out.println("Expected:\n"+json);
        json = gson.toJson(creator.getStateTransitionTable(), type);
        System.out.println("Actual:\n"+json);
    }
}
