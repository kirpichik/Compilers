package org.polushin.compilers.state_machine;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TransitionFunctionTest {

    public static Transit<Integer> tr(int from, char sub, int to) {
        return new Transit<>(from, sub, to);
    }

    public static <S> Transit<S> tr(int from, char sub) {
        return new Transit<>(from, sub, null);
    }

    public static Transit<Collection<Integer>> trc(int from, char sub, Integer... to) {
        return new Transit<>(from, sub, Arrays.asList(to));
    }

    public static <F extends TransitionFunction> F loadFunction(F function, String... lines) {
        if (lines.length == 1 && lines[0].isEmpty()) // Fix empty file
            lines[0] = "\n";
        try {
            function.loadTransitions(new StringReader(String.join("\n", lines)));
            return function;
        } catch (InvalidTransitionFunctionException e) {
            fail(e);
            return null;
        }
    }

    public static <F extends TransitionFunction<S>, S> void assertTransits(F function, Transit... transits) {
        for (Transit transit : transits) {
            Optional<S> result = function.transit(transit.from, transit.sub);
            if (transit.to == null)
                assertTrue(!result.isPresent());
            else {
                assertTrue(result.isPresent());
                if (transit.to instanceof Collection) {
                    final Collection to = (Collection) transit.to;
                    final Collection res;
                    try {
                        res = (Collection) result.get();
                    } catch (ClassCastException e) {
                        fail("Transition result is not expected collection", e);
                        return;
                    }
                    assertIterableEquals(to, res);
                } else
                    assertEquals(transit.to, result.get());
            }
        }
    }

    public static <F extends TransitionFunction> void assertFinals(F function, int... states) {
        for (int state : states)
            assertTrue(function.isStateFinal(state));
    }

    public static class Transit<T> {
        final int from;
        final T to;
        final char sub;

        public Transit(int from, char sub, T to) {
            this.from = from;
            this.to = to;
            this.sub = sub;
        }
    }

    TransitionFunction buildFunc() {
        return new AbstractTransitionFunction() {
            @Override
            public Optional transit(int currentState, char sub) {
                return Optional.empty();
            }

            @Override
            public void registerTransition(int from, char sub, int to) {

            }

            @Override
            public void unregisterTransition(int from, char sub, int to) {

            }
        };
    }

    @Test
    void finalStatesTest() {
        final TransitionFunction function = buildFunc();
        assertFalse(function.isStateFinal(1));
        function.markStateFinal(1);
        assertTrue(function.isStateFinal(1));
        function.unmarkStateFinal(1);
        assertFalse(function.isStateFinal(1));
    }
}