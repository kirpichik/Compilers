package org.polushin.compilers.state_machine;

import org.polushin.compilers.state_machine.dfa.DeterministicStateMachine;
import org.polushin.compilers.state_machine.dfa.DeterministicTransitionFunction;
import org.polushin.compilers.state_machine.nfa.NonDeterministicStateMachine;
import org.polushin.compilers.state_machine.nfa.NonDeterministicTransitionFunction;

import java.io.*;

public class Main {

    private static final int WRONG_ARGS = 1;
    private static final int FILE_NOT_FOUND = 2;
    private static final int UNEXPECTED_ERROR = 3;

    private static final String INPUT_VALID = "Input valid";
    private static final String INPUT_INVALID = "Input invalid";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Need args: [-d] <transition-description-file> [input-file]");
            System.exit(WRONG_ARGS);
        }

        final boolean isDeterministic = args[0].equals("-d");
        final String descriptionFile = args[isDeterministic ? 1 : 0];
        final int inputFilePos = isDeterministic ? 2 : 1;
        final String inputFile = args.length > inputFilePos ? args[inputFilePos] : null;

        executeValidation(prepareValidator(isDeterministic, descriptionFile), inputFile);
    }

    private static void executeValidation(StateMachine validator, String inputFile) {
        try (Reader input = inputFile == null ? new InputStreamReader(System.in) : new FileReader(inputFile)) {
            System.out.println(validator.validateInput(input) ? INPUT_VALID : INPUT_INVALID);
        } catch (FileNotFoundException e) {
            System.err.println(String.format("File '%s' not found.", inputFile));
            System.exit(FILE_NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(UNEXPECTED_ERROR);
        }
    }

    private static StateMachine prepareValidator(boolean isDeterministic, String descriptionFile) {
        try (Reader description = new FileReader(descriptionFile)) {
            return constructValidator(isDeterministic, description);
        } catch (FileNotFoundException e) {
            System.err.println(String.format("File '%s' not found.", descriptionFile));
            System.exit(FILE_NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(UNEXPECTED_ERROR);
        }
        return null;
    }

    private static StateMachine constructValidator(boolean isDeterministic, Reader description) {
        if (isDeterministic) {
            final DeterministicTransitionFunction function = new DeterministicTransitionFunction();
            function.loadTransitions(description);
            return new DeterministicStateMachine(function);
        } else {
            final NonDeterministicTransitionFunction function = new NonDeterministicTransitionFunction();
            function.loadTransitions(description);
            return new NonDeterministicStateMachine(function);
        }
    }

}
