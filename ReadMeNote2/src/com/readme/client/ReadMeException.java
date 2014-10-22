package com.readme.client;

import com.readme.json.JSONObject;


 
/**
 * 跟有道云API有关的异常
 *
 * @author haiwen
 */
public class ReadMeException extends Exception {
    protected static final String ERROR = "error";
    protected static final String MESSAGE = "message";

    private static final long serialVersionUID = 1L;

    protected int errorCode;

    public ReadMeException(JSONObject json) {
        super(json.getString(ReadMeException.MESSAGE));
        this.errorCode = json.getInt(ReadMeException.ERROR);
    }

    public ReadMeException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ReadMeException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ReadMeException(Throwable cause) {
        super(cause);
    }

    public int getErrorCode() {
        return errorCode;
    }
}

