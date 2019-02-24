package org.polushin.compilers.state_machine.nfa;

import org.polushin.compilers.state_machine.StateMachine;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class NonDeterministicStateMachine implements StateMachine {

    private final NonDeterministicTransitionFunction function;

    public NonDeterministicStateMachine(NonDeterministicTransitionFunction function) {
        this.function = function;
    }

    @Override
    public boolean validateInput(Reader reader) throws IOException {
        Collection<Integer> states = Collections.singleton(function.getInitialState());
        int input;

        while ((input = reader.read()) != -1) {
            final char sub = (char) input;
            states = states.stream()
                           .map(from -> function.transit(from, sub))
                           .filter(Optional::isPresent)
                           .map(Optional::get)
                           .flatMap(Collection::stream)
                           .collect(Collectors.toSet());
            if (states.isEmpty())
                return false;
        }

        return states.stream().anyMatch(function::isStateFinal);
    }
}
