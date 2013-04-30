package com.thoughtworks.yak;

public interface ServiceContainer {
    <T> T getService(Class<T> serviceType);
    <T> T getService(Class<T> serviceType, Enum key);
    boolean providesService(Class<?> service);
    boolean providesService(Class<?> service, Enum key);
}
