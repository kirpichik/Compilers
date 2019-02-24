package org.polushin.compilers.state_machine.nfa;

import org.polushin.compilers.state_machine.AbstractTransitionFunction;

import java.util.*;

public class NonDeterministicTransitionFunction extends AbstractTransitionFunction<Collection<Integer>> {

    private final Map<Integer, Map<Character, Collection<Integer>>> transitions = new HashMap<>();

    @Override
    public Optional<Collection<Integer>> transit(int currentState, char sub) {
        final Map<Character, Collection<Integer>> target = transitions.get(currentState);
        if (target == null)
            return Optional.empty();
        return Optional.ofNullable(target.get(sub));
    }

    @Override
    public void registerTransition(int from, char sub, int to) {
        transitions.computeIfAbsent(from, HashMap::new).computeIfAbsent(sub, HashSet::new).add(to);
    }

    @Override
    public void unregisterTransition(int from, char sub, int to) {
        final Map<Character, Collection<Integer>> targets = transitions.get(from);
        if (targets == null)
            return;
        final Collection<Integer> target = targets.get(sub);
        if (target == null)
            return;
        target.remove(to);
        if (target.isEmpty()) {
            targets.remove(sub);
            if (targets.isEmpty())
                transitions.remove(from);
        }
    }
}
