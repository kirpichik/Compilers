package org.polushin.compilers.state_machine;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Need args: <transition-description-file> [input-file]");
            System.exit(1);
        }

        final TransitionFunction function;
        try (Reader transitionsFile = new FileReader(args[0])) {
            function = TransitionFunction.readFrom(transitionsFile);
        } catch (FileNotFoundException e) {
            System.out.println("File " + args[0] + " not found.");
            System.exit(1);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return;
        }

        final StateMachine machine = new StateMachine(function);
        try (Reader input = args.length < 2 ? new InputStreamReader(System.in) : new FileReader(args[1])) {
            if (machine.validateInput(input))
                System.out.println("Input valid!");
            else {
                System.out.println("Input invalid!");
                System.exit(-1);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + args[0] + " not found.");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (NoSuchTransitionException e) {
            System.out.println(e.getMessage());
            System.exit(2);
        }
    }
}
