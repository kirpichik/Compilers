package org.polushin.compilers.arithm_exprs;

import java.io.IOException;
import java.text.ParseException;

public class Parser {

    private final Lexer lexer;
    private Lexeme curr;

    public Parser(Lexer lexer) throws IOException, ParseException {
        this.lexer = lexer;
        curr = lexer.nextLexeme();
    }

    public int executeCalculations() throws IOException, ParseException {
        return 0;
    }

    private int executeExpr() throws IOException, ParseException {
        return 0;
    }

    private int executeTerm() throws IOException, ParseException {
        return 0;
    }

    private int executeFractal() throws IOException, ParseException {
        return 0;
    }

    private int executePower() throws IOException, ParseException {
        return 0;
    }

    private int executeAtom() throws IOException, ParseException {
        return 0;
    }

}
