package org.polushin.compilers.arithm_exprs;

public class Lexeme {

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
        LEFT_BRACKET,
        RIGHT_BRACKET,
        EOF
    }
}
