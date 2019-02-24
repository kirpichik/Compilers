package org.polushin.compilers.state_machine;

public class InvalidTransitionFunctionException extends RuntimeException {

    public InvalidTransitionFunctionException() {

    }

    public InvalidTransitionFunctionException(Throwable cause) {
        super(cause);
    }

    public InvalidTransitionFunctionException(String message) {
        super(message);
    }

    public InvalidTransitionFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

}
