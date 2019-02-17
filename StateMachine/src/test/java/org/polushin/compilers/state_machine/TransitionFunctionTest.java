package org.polushin.compilers.state_machine;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransitionFunctionTest {

    State stf(int id) {
        return new State(id, true);
    }

    Transit tr(int from, char sub, int to) {
        return tr(new State(from, false), sub, new State(to, false));
    }

    Transit tr(int from, char sub) {
        return tr(new State(from, false), sub, null);
    }

    Transit tr(State from, char sub, State to) {
        return new Transit(from, to, sub);
    }

    Transit tr(int from, char sub, State to) {
        return new Transit(new State(from, false), to, sub);
    }

    void assertTransits(TransitionFunction function, Transit... transits) {
        for (Transit transit : transits) {
            Optional<State> result = function.transit(transit.from, transit.sub);
            if (transit.to == null)
                assertTrue(!result.isPresent());
            else {
                assertTrue(result.isPresent());
                assertEquals(transit.to, result.get());
            }
        }
    }

    TransitionFunction buildFunc(String... lines) {
        try {
            return TransitionFunction.readFrom(new StringReader(String.join("\n", lines)));
        } catch (InvalidTransitionFunctionException e) {
            fail(e);
            return null;
        }
    }

    @Test
    void transitEmpty() {
        assertTransits(buildFunc("\n"));
    }

    @Test
    void transitOnlyFinals() {
        buildFunc("1");
        buildFunc("1 2 3");
    }

    @Test
    void transitSingle() {
        assertTransits(buildFunc("", "1 a 2"), tr(1, 'a', 2));
        assertTransits(buildFunc("", "5 c 9"), tr(5, 'c', 9));
    }

    @Test
    void transitCheckFinals() {
        assertTransits(buildFunc("2", "1 a 2"), tr(1, 'a', stf(2)));
        assertTransits(buildFunc("5 9", "5 c 9"), tr(stf(5), 'c', stf(9)));
    }

    @Test
    void transitMultiRules() {
        assertTransits(buildFunc("", "1 a 2", "2 b 1"), tr(1, 'a', 2), tr(2, 'b', 1));
        assertTransits(buildFunc("", "5 c 9", "8 d 7", "42 z 199"), tr(5, 'c', 9), tr(8, 'd', 7), tr(42, 'z', 199));
    }

    @Test
    void transitHardRule() {
        assertTransits(buildFunc("", "1 test 2"), tr(1, 't', 2), tr(1, 'e', 2), tr(1, 's', 2), tr(1, 't', 2));
    }

    @Test
    void transitCheckRedundant() {
        assertTransits(buildFunc("", "1 a 2"), tr(1, 'b'), tr(2, 'a'));
    }

    void assertInvalid(String... lines) {
        assertThrows(InvalidTransitionFunctionException.class,
                     () -> TransitionFunction.readFrom(new StringReader(String.join("\n", lines))));
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

    class Transit {
        final State from, to;
        final char sub;

        Transit(State from, State to, char sub) {
            this.from = from;
            this.to = to;
            this.sub = sub;
        }
    }
}