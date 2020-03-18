package sample;

public class UnableToCreateFileException extends Exception {
    public UnableToCreateFileException() {
        super("Unable to create file");
    }
}
