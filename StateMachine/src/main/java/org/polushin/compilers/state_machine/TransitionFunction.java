package org.polushin.compilers.state_machine;

import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class TransitionFunction {

    private final Map<Integer, Map<Character, Integer>> transitions;
    private final Collection<Integer> finalStates;

    private TransitionFunction(Map<Integer, Map<Character, Integer>> transitions, Collection<Integer> finalStates) {
        this.transitions = transitions;
        this.finalStates = finalStates;
    }

    public State getInitialState() {
        return new State(1, finalStates.contains(1));
    }

    public Optional<State> transit(State currentState, char sub) {
        Map<Character, Integer> target = transitions.get(currentState.getId());
        if (target == null)
            return Optional.empty();
        Integer state = target.get(sub);
        if (state == null)
            return Optional.empty();
        return Optional.of(new State(state, finalStates.contains(state)));
    }

    public static TransitionFunction readFrom(Reader reader) {
        return null;
    }
}
