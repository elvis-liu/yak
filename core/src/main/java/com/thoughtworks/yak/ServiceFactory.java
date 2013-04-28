package com.thoughtworks.yak;

public interface ServiceFactory <T> {
    T createService(DeanContainer container, ServiceDefinition<T> serviceDefinition);
}
