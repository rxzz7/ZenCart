package com.zencart.user_service.exception;

public class AddressNotFoundException extends RuntimeException {

    public static final long SerialVersionUID = 1L;


    public AddressNotFoundException(String message, Throwable cause){super(message,cause);}

    public AddressNotFoundException(){super();}

    public AddressNotFoundException(Throwable cause){super(cause);}

    public AddressNotFoundException(String message) {
        super(message);
    }

}
