package org.polushin.compilers.state_machine;

import java.io.IOException;
import java.io.Reader;

public interface StateMachine {

    boolean validateInput(Reader reader) throws IOException;

}
