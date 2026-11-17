package com.mycompany.jacinto_bookbridgefinal;

/**
 *
 * @author Gail Jacinto
 */
public class BookBridgeException extends Exception {
    public BookBridgeException(String message) {
        super(message);
    }
    public BookBridgeException(String message, Throwable cause) {
        super(message, cause);
    }
}