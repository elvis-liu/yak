package com.thoughtworks.yak.exceptions;

import java.lang.reflect.InvocationTargetException;

public class ServiceInstantiationException extends RuntimeException {
    public ServiceInstantiationException(Throwable throwable) {
        super(throwable);
    }
}
