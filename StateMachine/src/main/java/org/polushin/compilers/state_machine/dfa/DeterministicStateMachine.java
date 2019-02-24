package org.polushin.compilers.state_machine.dfa;

import org.polushin.compilers.state_machine.StateMachine;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;

public class DeterministicStateMachine implements StateMachine {

    private final DeterministicTransitionFunction function;

    public DeterministicStateMachine(DeterministicTransitionFunction function) {
        this.function = function;
    }

    @Override
    public boolean validateInput(Reader reader) throws IOException {
        int state = function.getInitialState();
        int input;

        while ((input = reader.read()) != -1) {
            Optional<Integer> result = function.transit(state, (char) input);
            if (result.isPresent())
                state = result.get();
            else
                return false;
        }

        return function.isStateFinal(state);
    }

}
