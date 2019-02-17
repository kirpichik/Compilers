package org.polushin.compilers.state_machine;

import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransitionFunction {

    private final Map<Integer, Map<Character, Integer>> transitions;
    private final Collection<Integer> finalStates;

    private TransitionFunction(Map<Integer, Map<Character, Integer>> transitions, Collection<Integer> finalStates) {
        this.transitions = transitions;
        this.finalStates = finalStates;
    }

    public State getInitialState() {
        return new State(1, finalStates.contains(1));
    }

    public Optional<State> transit(State currentState, char sub) {
        Map<Character, Integer> target = transitions.get(currentState.getId());
        if (target == null)
            return Optional.empty();
        Integer state = target.get(sub);
        if (state == null)
            return Optional.empty();
        return Optional.of(new State(state, finalStates.contains(state)));
    }

    public static TransitionFunction readFrom(Reader reader) throws InvalidTransitionFunctionException {
        Map<Integer, Map<Character, Integer>> transitions = new HashMap<>();
        Scanner scanner = new Scanner(reader);
        Collection<Integer> finalStates;

        try {
            finalStates = Stream.of(scanner.nextLine().split(" ")).filter(s -> !s.isEmpty()).map(Integer::parseInt)
                    .collect(Collectors.toSet());

            while (scanner.hasNextLine()) {
                int from = scanner.nextInt();
                String str = scanner.next("[a-zA-Z]+");
                int to = scanner.nextInt();
                Map<Character, Integer> map = transitions.computeIfAbsent(from, k -> new HashMap<>());
                str.chars().forEach((c) -> map.put((char) c, to));
            }
        } catch (NumberFormatException | NoSuchElementException e) {
            throw new InvalidTransitionFunctionException(e);
        }

        return new TransitionFunction(transitions, finalStates);
    }
}
