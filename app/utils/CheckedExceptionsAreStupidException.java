package utils;

/**
 * This class lets you know that checked exceptions are stupid, in case you
 * were too dumb to realize that already.  Also, it conveniently wraps checked
 * exceptions in a runtime exception so we don't have to mess around with try catch
 * blocks in all the parts of the codebase that don't actually care.
 * 
 * @author Lawrence McAlpin
 */
public class CheckedExceptionsAreStupidException extends RuntimeException {

    public CheckedExceptionsAreStupidException() {
        super();
    }

    public CheckedExceptionsAreStupidException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public CheckedExceptionsAreStupidException(String arg0) {
        super(arg0);
    }

    public CheckedExceptionsAreStupidException(Throwable arg0) {
        super(arg0);
    }

}
