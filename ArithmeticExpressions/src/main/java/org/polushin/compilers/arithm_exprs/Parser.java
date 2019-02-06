package org.polushin.compilers.arithm_exprs;

import java.io.IOException;
import java.text.ParseException;

import static org.polushin.compilers.arithm_exprs.Lexeme.Type.*;

public class Parser {

    private final Lexer lexer;
    private Lexeme curr;

    public Parser(Lexer lexer) throws IOException, ParseException {
        this.lexer = lexer;
        curr = lexer.nextLexeme();
    }

    public int executeCalculations() throws IOException, ParseException {
        int result = executeExpr();
        if (curr.type != EOF)
            throw new ParseException("Expected EOF", 0);
        return result;
    }

    private int executeExpr() throws IOException, ParseException {
        int result = executeTerm();
        Lexeme.Type type = curr.type;

        while (type == PLUS || type == MINUS) {
            curr = lexer.nextLexeme();
            if (type == PLUS)
                result += executeTerm();
            else
                result -= executeTerm();
            type = curr.type;
        }

        return result;
    }

    private int executeTerm() throws IOException, ParseException {
        int result = executeFractal();
        Lexeme.Type type = curr.type;

        while (type == MULTIPLICATION || type == DIVISION) {
            curr = lexer.nextLexeme();
            if (type == MULTIPLICATION)
                result *= executeFractal();
            else
                result /= executeFractal();
            type = curr.type;
        }

        return result;
    }

    private int executeFractal() throws IOException, ParseException {
        int result = executePower();
        if (curr.type == POWER) {
            curr = lexer.nextLexeme();
            return (int) Math.pow(result, executeFractal());
        }
        return result;
    }

    private int executePower() throws IOException, ParseException {
        if (curr.type == MINUS) {
            curr = lexer.nextLexeme();
            return -executeAtom();
        }
        return executeAtom();
    }

    private int executeAtom() throws IOException, ParseException {
        if (curr.type == NUMBER) {
            int result = Integer.parseInt(curr.value);
            curr = lexer.nextLexeme();
            return result;
        } else if (curr.type == LEFT_BRACKET) {
            curr = lexer.nextLexeme();
            int result = executeExpr();
            if (curr.type != RIGHT_BRACKET)
                throw new ParseException("Expected close bracket", 0);
            curr = lexer.nextLexeme();
            return result;
        } else
            throw new ParseException("Unexpected lexeme type: " + curr.type.name(), 0);
    }

}
