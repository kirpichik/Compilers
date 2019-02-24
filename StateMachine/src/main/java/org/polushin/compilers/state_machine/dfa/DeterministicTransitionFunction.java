package org.polushin.compilers.state_machine.dfa;

import org.polushin.compilers.state_machine.AbstractTransitionFunction;
import org.polushin.compilers.state_machine.InvalidTransitionFunctionException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DeterministicTransitionFunction extends AbstractTransitionFunction<Integer> {

    private final Map<Integer, Map<Character, Integer>> transitions = new HashMap<>();

    @Override
    public Optional<Integer> transit(int currentState, char sub) {
        final Map<Character, Integer> target = transitions.get(currentState);
        if (target == null)
            return Optional.empty();
        return Optional.ofNullable(target.get(sub));
    }

    @Override
    public void registerTransition(int from, char sub, int to) throws InvalidTransitionFunctionException {
        final Map<Character, Integer> target = transitions.computeIfAbsent(from, HashMap::new);
        if (target.containsKey(sub))
            throw new InvalidTransitionFunctionException("Multiply transitions in deterministic state machine");
        target.put(sub, to);
    }

    @Override
    public void unregisterTransition(int from, char sub, int to) {
        final Map<Character, Integer> target = transitions.get(from);
        if (target == null)
            return;
        target.remove(sub);
        if (target.isEmpty())
            transitions.remove(from);
    }
}
