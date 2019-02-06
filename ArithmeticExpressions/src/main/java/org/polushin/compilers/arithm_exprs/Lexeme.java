package org.polushin.compilers.arithm_exprs;

public class Lexeme {

    public static final Lexeme PLUS = new Lexeme(Type.PLUS, "+");
    public static final Lexeme MINUS = new Lexeme(Type.MINUS, "-");
    public static final Lexeme MULTIPLICATION = new Lexeme(Type.MULTIPLICATION, "*");
    public static final Lexeme DIVISION = new Lexeme(Type.DIVISION, "/");
    public static final Lexeme POWER = new Lexeme(Type.POWER, "^");
    public static final Lexeme LEFT_BRACKET = new Lexeme(Type.LEFT_BRACKET, "(");
    public static final Lexeme RIGHT_BRACKET = new Lexeme(Type.RIGHT_BRACKET, ")");
    public static final Lexeme EOF = new Lexeme(Type.EOF, null);

    public final Type type;
    public final String value;

    public Lexeme(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public enum Type {
        NUMBER,
        PLUS,
        MINUS,
        MULTIPLICATION,
        DIVISION,
        POWER,
        LEFT_BRACKET,
        RIGHT_BRACKET,
        EOF
    }
}
