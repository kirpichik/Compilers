package org.polushin.compilers.arithm_exprs;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.polushin.compilers.arithm_exprs.Lexeme.Type.*;

class LexerTest {

    void assertLexemeTypes(String input, Lexeme.Type... types) throws IOException, ParseException {
        try (Lexer lexer = new Lexer(new StringReader(input))) {
            for (Lexeme.Type type : types)
                assertEquals(type, lexer.nextLexeme().type);
        }
    }

    void assertLexemeValues(String input, String... values) throws IOException, ParseException {
        try (Lexer lexer = new Lexer(new StringReader(input))) {
            for (String value : values)
                assertEquals(value, lexer.nextLexeme().value);
        }
    }

    @Test
    void nextLexemeSimpleSingleTypes() throws IOException, ParseException {
        assertLexemeTypes("", EOF);
        assertLexemeTypes(" ", EOF);
        assertLexemeTypes("    ", EOF);
        assertLexemeTypes("\t", EOF);
        assertLexemeTypes("\t\t\t\t", EOF);
        assertLexemeTypes("\t \t  \t   \t    ", EOF);
        assertLexemeTypes("(", LEFT_BRACKET, EOF);
        assertLexemeTypes(")", RIGHT_BRACKET, EOF);
        assertLexemeTypes("+", PLUS, EOF);
        assertLexemeTypes("-", MINUS, EOF);
        assertLexemeTypes("*", MULTIPLICATION, EOF);
        assertLexemeTypes("/", DIVISION, EOF);
        assertLexemeTypes("^", POWER, EOF);
    }

    @Test
    void nextLexemeSimpleNumberTypes() throws IOException, ParseException {
        assertLexemeTypes("0", NUMBER, EOF);
        assertLexemeTypes("1", NUMBER, EOF);
        assertLexemeTypes("2", NUMBER, EOF);
        assertLexemeTypes("3", NUMBER, EOF);
        assertLexemeTypes("4", NUMBER, EOF);
        assertLexemeTypes("5", NUMBER, EOF);
        assertLexemeTypes("6", NUMBER, EOF);
        assertLexemeTypes("7", NUMBER, EOF);
        assertLexemeTypes("8", NUMBER, EOF);
        assertLexemeTypes("9", NUMBER, EOF);
        assertLexemeTypes("123", NUMBER, EOF);
        assertLexemeTypes("0123456789", NUMBER, EOF);
        assertLexemeTypes("0987654321", NUMBER, EOF);
    }

    @Test
    void nextLexemeSimpleNumberValues() throws IOException, ParseException {
        assertLexemeValues("0", "0");
        assertLexemeValues("1", "1");
        assertLexemeValues("2", "2");
        assertLexemeValues("3", "3");
        assertLexemeValues("4", "4");
        assertLexemeValues("5", "5");
        assertLexemeValues("6", "6");
        assertLexemeValues("7", "7");
        assertLexemeValues("8", "8");
        assertLexemeValues("9", "9");
        assertLexemeValues("123", "123");
        assertLexemeValues("0123456789", "0123456789");
        assertLexemeValues("9876543210", "9876543210");
    }

    @Test
    void nextLexemeComplexShortTypes() throws IOException, ParseException {
        assertLexemeTypes("()+-*/^", LEFT_BRACKET, RIGHT_BRACKET, PLUS, MINUS, MULTIPLICATION, DIVISION, POWER, EOF);

        assertLexemeTypes("((((", LEFT_BRACKET, LEFT_BRACKET, LEFT_BRACKET, LEFT_BRACKET, EOF);
        assertLexemeTypes("))))", RIGHT_BRACKET, RIGHT_BRACKET, RIGHT_BRACKET, RIGHT_BRACKET, EOF);
        assertLexemeTypes("++++", PLUS, PLUS, PLUS, PLUS, EOF);
        assertLexemeTypes("----", MINUS, MINUS, MINUS, MINUS, EOF);
        assertLexemeTypes("****", MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, EOF);
        assertLexemeTypes("////", DIVISION, DIVISION, DIVISION, DIVISION, EOF);
        assertLexemeTypes("^^^^", POWER, POWER, POWER, POWER, EOF);

        assertLexemeTypes(" ( ( ( ( ", LEFT_BRACKET, LEFT_BRACKET, LEFT_BRACKET, LEFT_BRACKET, EOF);
        assertLexemeTypes(" ) ) ) ) ", RIGHT_BRACKET, RIGHT_BRACKET, RIGHT_BRACKET, RIGHT_BRACKET, EOF);
        assertLexemeTypes(" + + + + ", PLUS, PLUS, PLUS, PLUS, EOF);
        assertLexemeTypes(" - - - - ", MINUS, MINUS, MINUS, MINUS, EOF);
        assertLexemeTypes(" * * * * ", MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, EOF);
        assertLexemeTypes(" / / / / ", DIVISION, DIVISION, DIVISION, DIVISION, EOF);
        assertLexemeTypes(" ^ ^ ^ ^ ", POWER, POWER, POWER, POWER, EOF);

        assertLexemeTypes("\t(\t(\t(\t(\t", LEFT_BRACKET, LEFT_BRACKET, LEFT_BRACKET, LEFT_BRACKET, EOF);
        assertLexemeTypes("\t)\t)\t)\t)\t", RIGHT_BRACKET, RIGHT_BRACKET, RIGHT_BRACKET, RIGHT_BRACKET, EOF);
        assertLexemeTypes("\t+\t+\t+\t+\t", PLUS, PLUS, PLUS, PLUS, EOF);
        assertLexemeTypes("\t-\t-\t-\t-\t", MINUS, MINUS, MINUS, MINUS, EOF);
        assertLexemeTypes("\t*\t*\t*\t*\t", MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, EOF);
        assertLexemeTypes("\t/\t/\t/\t/\t", DIVISION, DIVISION, DIVISION, DIVISION, EOF);
        assertLexemeTypes("\t^\t^\t^\t^\t", POWER, POWER, POWER, POWER, EOF);

        assertLexemeTypes("\t ( \t( \t (  \t  ( \t \t \t", LEFT_BRACKET, LEFT_BRACKET, LEFT_BRACKET, LEFT_BRACKET, EOF);
        assertLexemeTypes("\t ) \t) \t )  \t  ) \t \t \t", RIGHT_BRACKET, RIGHT_BRACKET, RIGHT_BRACKET, RIGHT_BRACKET,
                          EOF);
        assertLexemeTypes("\t + \t+ \t +  \t  + \t \t \t", PLUS, PLUS, PLUS, PLUS, EOF);
        assertLexemeTypes("\t - \t- \t -  \t  - \t \t \t", MINUS, MINUS, MINUS, MINUS, EOF);
        assertLexemeTypes("\t * \t* \t *  \t  * \t \t \t", MULTIPLICATION, MULTIPLICATION, MULTIPLICATION,
                          MULTIPLICATION, EOF);
        assertLexemeTypes("\t / \t/ \t /  \t  / \t \t \t", DIVISION, DIVISION, DIVISION, DIVISION, EOF);
        assertLexemeTypes("\t ^ \t^ \t ^  \t  ^ \t \t \t", POWER, POWER, POWER, POWER, EOF);
    }

    @Test
    void nextLexemeComplexNumberTypes() throws IOException, ParseException {
        assertLexemeTypes("++123--", PLUS, PLUS, NUMBER, MINUS, MINUS, EOF);
        assertLexemeTypes("* 123 /", MULTIPLICATION, NUMBER, DIVISION, EOF);
        assertLexemeTypes("^^ \t 000 \t ^^", POWER, POWER, NUMBER, POWER, POWER, EOF);
    }

    @Test
    void nextLexemeComplexNumberValues() throws IOException, ParseException {
        assertLexemeValues("++123--", "+", "+", "123", "-", "-");
        assertLexemeValues("* 123 /", "*", "123", "/");
        assertLexemeValues("^^ \t 000 \t ^^", "^", "^", "000", "^", "^");
    }

    @Test
    void nextLexemeComplexLong() throws IOException, ParseException {
        assertLexemeTypes("18 +\t128*42   ^3 \t", NUMBER, PLUS, NUMBER, MULTIPLICATION, NUMBER, POWER, NUMBER, EOF);
        assertLexemeTypes("1+++4^^3 15 20", NUMBER, PLUS, PLUS, PLUS, NUMBER, POWER, POWER, NUMBER, NUMBER, NUMBER,
                          EOF);
    }
}