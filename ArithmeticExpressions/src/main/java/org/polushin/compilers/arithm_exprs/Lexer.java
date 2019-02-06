package org.polushin.compilers.arithm_exprs;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;

public class Lexer implements AutoCloseable {

    private final Reader reader;
    private int curr;

    public Lexer(Reader reader) throws IOException {
        this.reader = reader;
        curr = reader.read();
    }

    public Lexeme nextLexeme() throws IOException, ParseException {
        return null;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
