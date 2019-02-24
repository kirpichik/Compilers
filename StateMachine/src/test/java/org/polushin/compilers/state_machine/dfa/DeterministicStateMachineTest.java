package org.polushin.compilers.state_machine.dfa;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class DeterministicStateMachineTest {

    void assertValidation(String input, String... lines) {
        try {
            final DeterministicTransitionFunction function = DeterministicTransitionFunctionTest.buildFunc(lines);
            final DeterministicStateMachine validator = new DeterministicStateMachine(function);
            assertTrue(validator.validateInput(new StringReader(input)));
        } catch (IOException e) {
            fail(e);
        }
    }

    void assertInvalidation(String input, String... lines) {
        try {
            final DeterministicTransitionFunction function = DeterministicTransitionFunctionTest.buildFunc(lines);
            final DeterministicStateMachine validator = new DeterministicStateMachine(function);
            assertFalse(validator.validateInput(new StringReader(input)));
        } catch (IOException e) {
            fail(e);
        }
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
        assertInvalidation("abc", "2", "1 d 2");
        assertInvalidation("def", "", "");
    }

}