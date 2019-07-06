package nl.mesoplz.hue.exceptions;

public class BridgeNotFoundException extends HueException{

    public BridgeNotFoundException() {
    }

    public BridgeNotFoundException(String message) {
        super(message);
    }
}
