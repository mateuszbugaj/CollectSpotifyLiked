package sample;

import java.io.IOException;

public class CannotOpenLoginWindowException extends Exception {
    public CannotOpenLoginWindowException() {
        super("Cannot open login window.");
    }
}
