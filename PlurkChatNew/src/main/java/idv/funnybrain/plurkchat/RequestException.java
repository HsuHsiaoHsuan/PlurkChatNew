package idv.funnybrain.plurkchat;

public class RequestException extends Exception {

    public RequestException(Exception e) {
        super(e);
    }

    public RequestException(String message) {
        super(message);
    }

}
