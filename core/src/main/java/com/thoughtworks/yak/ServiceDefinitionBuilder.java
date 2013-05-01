package com.thoughtworks.yak;

import java.lang.reflect.Modifier;

public class ServiceDefinitionBuilder <T> {
    private Class<T> service;
    private Class<? extends T> serviceProvider;
    private Enum key;

    ServiceDefinitionBuilder(Class<T> service) {
        if (!isAllowedServiceType(service)) {
            throw new IllegalArgumentException("Service type can only be interface or abstract class, not: " + service);
        }
        this.service = service;
    }

    private static boolean isAllowedServiceType(Class<?> service) {
        return service.isInterface() || Modifier.isAbstract(service.getModifiers());
    }

    private static boolean isAllowedServiceProviderType(Class<?> serviceProvider) {
        if (serviceProvider.isInterface()) {
            return false;
        }

        if (Modifier.isAbstract(serviceProvider.getModifiers())) {
            return false;
        }

        if (!Modifier.isPublic(serviceProvider.getModifiers())) {
            return false;
        }

        if (serviceProvider.getEnclosingClass() != null && !Modifier.isStatic(serviceProvider.getModifiers())) {
            return false;
        }

        //noinspection RedundantIfStatement
        if (serviceProvider.getConstructors().length == 0) {
            return false;
        }

        return true;
    }

    public ServiceDefinitionBuilder<T> implementedBy(Class<? extends T> serviceProvider) {
        if (!isAllowedServiceProviderType(serviceProvider)) {
            throw new IllegalArgumentException("Service provider class must allow create instance: " + serviceProvider);
        }

        if (!service.isAssignableFrom(serviceProvider)) {
            throw new IllegalArgumentException("Service provider must implements the Service: " + serviceProvider);
        }

        this.serviceProvider = serviceProvider;

        return this;
    }

    public ServiceDefinition<T> build() {
        return new ServiceDefinition<T>(service, serviceProvider, key);
    }

    public ServiceDefinitionBuilder<T> withKey(Enum key) {
        this.key = key;
        return this;
    }
}
