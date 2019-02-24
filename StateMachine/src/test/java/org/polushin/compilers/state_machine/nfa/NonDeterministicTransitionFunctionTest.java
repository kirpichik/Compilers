package org.polushin.compilers.state_machine.nfa;

import org.junit.jupiter.api.Test;
import org.polushin.compilers.state_machine.InvalidTransitionFunctionException;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.polushin.compilers.state_machine.TransitionFunctionTest.*;

public class NonDeterministicTransitionFunctionTest {

    public static NonDeterministicTransitionFunction buildFunc(String... lines) {
        final NonDeterministicTransitionFunction function = new NonDeterministicTransitionFunction();
        loadFunction(function, lines);
        return function;
    }

    void assertInvalid(String... lines) {
        assertThrows(InvalidTransitionFunctionException.class, () -> {
            final NonDeterministicTransitionFunction function = new NonDeterministicTransitionFunction();
            function.loadTransitions(new StringReader(String.join("\n", lines)));
        });
    }

    @Test
    void transitEmpty() {
        assertTransits(buildFunc(""));
    }

    @Test
    void transitSingle() {
        assertTransits(buildFunc("", "1 a 2"), trc(1, 'a', 2));
        assertTransits(buildFunc("", "5 c 9"), trc(5, 'c', 9));
    }

    @Test
    void transitCheckFinals() {
        assertFinals(buildFunc("2"), 2);
        assertFinals(buildFunc("5 9"), 5, 9);
        assertFinals(buildFunc("1 3", "1 a 2"), 1, 3);
    }

    @Test
    void transitMultiRules() {
        assertTransits(buildFunc("", "1 a 2", "2 b 1"), trc(1, 'a', 2), trc(2, 'b', 1));
        assertTransits(buildFunc("", "5 c 9", "8 d 7", "42 z 199"), trc(5, 'c', 9), trc(8, 'd', 7), trc(42, 'z', 199));
    }

    @Test
    void transitHardRule() {
        assertTransits(buildFunc("", "1 test 2"), trc(1, 't', 2), trc(1, 'e', 2), trc(1, 's', 2), trc(1, 't', 2));
    }

    @Test
    void transitCheckRedundant() {
        assertTransits(buildFunc("", "1 a 2"), tr(1, 'b'), tr(2, 'a'));
    }

    @Test
    void transitInvalid() {
        assertInvalid();
        assertInvalid("test");
        assertInvalid("", "test a 2");
        assertInvalid("", "2 2 2");
        assertInvalid("", "2");
        assertInvalid("", "2 a");
    }

    @Test
    void transitRepeats() {
        assertTransits(buildFunc("", "1 a 2", "1 a 2"), trc(1, 'a', 2));
        assertTransits(buildFunc("", "1 a 2", "1 a 3"), trc(1, 'a', 2, 3));
    }

}