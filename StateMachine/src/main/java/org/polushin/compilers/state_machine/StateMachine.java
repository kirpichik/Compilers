package org.polushin.compilers.state_machine;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

public class StateMachine {

    private final TransitionFunction transitions;

    public StateMachine(TransitionFunction transitions) {
        Objects.requireNonNull(transitions, "Transitions function cannot be null!");
        this.transitions = transitions;
    }

    public boolean validateInput(Reader reader) throws IOException, NoSuchTransitionException {
        State state = transitions.getInitialState();
        int input;

        while ((input = reader.read()) != -1) {
            final char sub = (char) input;
            final int stateId = state.getId();
            state = transitions.transit(state, sub).orElseThrow(() -> new NoSuchTransitionException(stateId, sub));
        }

        return state.isFinal();
    }
}
