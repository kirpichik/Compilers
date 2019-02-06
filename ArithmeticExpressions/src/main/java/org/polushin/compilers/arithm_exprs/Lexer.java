package org.polushin.compilers.arithm_exprs;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;

import static org.polushin.compilers.arithm_exprs.Lexeme.Type.*;

public class Lexer implements AutoCloseable {

    private final Reader reader;
    private int curr;
    private int pos;

    public Lexer(Reader reader) throws IOException {
        this.reader = reader;
        buffNextChar();
    }

    public Lexeme nextLexeme() throws IOException, ParseException {
        while (Character.isWhitespace(curr))
            buffNextChar();

        switch (curr) {
            case -1:
                return new Lexeme(EOF);
            case '(':
                buffNextChar();
                return new Lexeme(LEFT_BRACKET);
            case ')':
                buffNextChar();
                return new Lexeme(RIGHT_BRACKET);
            case '+':
                buffNextChar();
                return new Lexeme(PLUS);
            case '-':
                buffNextChar();
                return new Lexeme(MINUS);
            case '*':
                buffNextChar();
                return new Lexeme(MULTIPLICATION);
            case '/':
                buffNextChar();
                return new Lexeme(DIVISION);
            case '^':
                buffNextChar();
                return new Lexeme(POWER);
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return readNumber();
            default:
                throw new ParseException(String.format("Unexpected char: '%c'", curr), pos);
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    private Lexeme readNumber() throws IOException {
        StringBuilder number = new StringBuilder();

        do {
            number.append((char) curr);
            buffNextChar();
        } while (Character.isDigit(curr));

        return new Lexeme(NUMBER, number.toString());
    }

    private int buffNextChar() throws IOException {
        curr = reader.read();
        pos++;
        return curr;
    }
}
