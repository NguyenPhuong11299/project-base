package com.automation.exceptions;

public class InvalidPathForExcelException extends InvalidPathForFilesException{

    /**
     * Pass the message that needs to be appended to the stacktrace
     * @param message Details about the exception or custom message
     */
    public InvalidPathForExcelException(String message) {
        super(message);
    }

}
