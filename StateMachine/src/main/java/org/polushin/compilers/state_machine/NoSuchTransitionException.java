package org.polushin.compilers.state_machine;

public class NoSuchTransitionException extends Exception {

    public NoSuchTransitionException(int state, char sub) {
        super(String.format("Unknown transition from %d state by '%c'", state, sub));
    }
}
