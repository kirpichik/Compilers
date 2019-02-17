package org.polushin.compilers.state_machine;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class StateMachineTest {

    TransitionFunction buildFunc(String... lines) throws InvalidTransitionFunctionException {
        if (lines.length == 1 && lines[0].isEmpty()) // Fix empty file
            lines[0] = "\n";
        return TransitionFunction.readFrom(new StringReader(String.join("\n", lines)));
    }

    void assertValidation(String input, String... lines) {
        try {
            assertTrue(new StateMachine(buildFunc(lines)).validateInput(new StringReader(input)));
        } catch (IOException | NoSuchTransitionException | InvalidTransitionFunctionException e) {
            fail(e);
        }
    }

    void assertInvalidation(String input, String... lines) {
        try {
            assertFalse(new StateMachine(buildFunc(lines)).validateInput(new StringReader(input)));
        } catch (IOException | NoSuchTransitionException | InvalidTransitionFunctionException e) {
            fail(e);
        }
    }

    void execValidation(String input, String... lines) {
        assertThrows(NoSuchTransitionException.class,
                     () -> new StateMachine(buildFunc(lines)).validateInput(new StringReader(input)));
    }

    @Test
    void validateInputEmpty() {
        assertValidation("", "1");
    }

    @Test
    void validateInputSingle() {
        assertValidation("a", "2", "1 a 2");
        assertValidation("a", "1", "1 a 1");
    }

    @Test
    void validateInputMulti() {
        assertValidation("abc", "4", "1 a 2", "2 b 3", "3 c 4");
        assertValidation("abc", "1", "1 a 2", "2 b 3", "3 c 1");
        assertValidation("abbbc", "3", "1 a 2", "2 b 2", "2 c 3");
    }

    @Test
    void validateInputEmptyUnfinished() {
        assertInvalidation("", "");
        assertInvalidation("", "2");
    }

    @Test
    void validateInputSingleUnfinished() {
        assertInvalidation("a", "1", "1 a 2");
        assertInvalidation("a", "2", "1 a 1");
    }

    @Test
    void validateInputMultiUnfinished() {
        assertInvalidation("abc", "1 2 3", "1 a 2", "2 b 3", "3 c 4");
        assertInvalidation("abc", "3 2", "1 a 2", "2 b 3", "3 c 1");
        assertInvalidation("abbbc", "1", "1 a 2", "2 b 2", "2 c 3");
    }

    @Test
    void validateInputUnknownSymbol() {
        execValidation("abc", "2", "1 d 2");
        execValidation("def", "", "");
    }
}