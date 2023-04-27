package com.sgyj.userservice.exception;

import com.sgyj.userservice.enums.ErrorMessage;

public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException() {
        super( ErrorMessage.VALIDATE_REFRESH_TOKEN.getMessage() );
    }
    public ExpiredTokenException( String s ) {
        super(s);
    }

}
