package org.polushin.compilers.state_machine;

import java.io.Reader;
import java.util.Optional;
import java.util.regex.Pattern;

public interface TransitionFunction<T> {

    Pattern SYMBOLS_PATTERN = Pattern.compile("[a-zA-Z]+");

    int getInitialState();

    Optional<T> transit(int currentState, char sub);

    void registerTransition(int from, char sub, int to) throws InvalidTransitionFunctionException;

    void markStateFinal(int state);

    void unregisterTransition(int from, char sub, int to);

    void unmarkStateFinal(int state);

    boolean isStateFinal(int state);

    void loadTransitions(Reader reader) throws InvalidTransitionFunctionException;

}
