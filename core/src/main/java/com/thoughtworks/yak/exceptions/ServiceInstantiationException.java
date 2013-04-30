package com.thoughtworks.yak.exceptions;

public class ServiceInstantiationException extends RuntimeException {
    public ServiceInstantiationException(Throwable throwable) {
        super(throwable);
    }

    public ServiceInstantiationException(String message) {
        super(message);
    }
}
