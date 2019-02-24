package org.polushin.compilers.state_machine;

import java.io.Reader;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Stream;

public abstract class AbstractTransitionFunction<T> implements TransitionFunction<T> {

    private final Collection<Integer> finalStates = new HashSet<>();

    @Override
    public int getInitialState() {
        return 1;
    }

    @Override
    public void markStateFinal(int state) {
        finalStates.add(state);
    }

    @Override
    public void unmarkStateFinal(int state) {
        finalStates.remove(state);
    }

    @Override
    public boolean isStateFinal(int state) {
        return finalStates.contains(state);
    }

    @Override
    public void loadTransitions(Reader reader) throws InvalidTransitionFunctionException {
        final Scanner scanner = new Scanner(reader);

        try {
            Stream.of(scanner.nextLine().split(" "))
                  .filter(s -> !s.isEmpty())
                  .map(Integer::parseInt)
                  .forEach(this::markStateFinal);

            while (scanner.hasNextLine()) {
                final int from = scanner.nextInt();
                final String str = scanner.next(SYMBOLS_PATTERN);
                final int to = scanner.nextInt();
                str.chars().forEach(c -> registerTransition(from, (char) c, to));
            }
        } catch (NumberFormatException | NoSuchElementException e) {
            throw new InvalidTransitionFunctionException(e);
        }
    }
}
