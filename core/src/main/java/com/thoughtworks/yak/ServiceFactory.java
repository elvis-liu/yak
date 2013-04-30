package com.thoughtworks.yak;

public interface ServiceFactory <T> {
    T createService(ServiceContainer container, ServiceDefinition<T> serviceDefinition);
}
