package org.lostboyhubble.xiffer.troubleshooting;

public class InvalidXifferStatementException extends RuntimeException
{
    public InvalidXifferStatementException() {
        super();
    }

    public InvalidXifferStatementException(String message) {
        super(message);
    }

    public InvalidXifferStatementException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidXifferStatementException(Throwable cause) {
        super(cause);
    }
}
