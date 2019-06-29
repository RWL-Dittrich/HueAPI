package nl.mesoplz.hue.exceptions;

public class InvalidCommandException extends Exception {

    public InvalidCommandException(String reason) {
        super(reason);
    }

}
