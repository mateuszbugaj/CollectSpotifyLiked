package sample;

public class UnableToLogInException extends Exception{
    public UnableToLogInException() {
        super("Unable to log in.");
    }
}
