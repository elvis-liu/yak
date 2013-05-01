package com.thoughtworks.yak;

import java.util.HashSet;
import java.util.Set;

public abstract class ServiceModule {
    private final Set<ServiceDefinition<?>> serviceDefinitions = new HashSet<ServiceDefinition<?>>();
    private final Set<ServiceDefinitionBuilder<?>> configuredBuilders = new HashSet<ServiceDefinitionBuilder<?>>();

    public <T> ServiceDefinition<T> getService(Class<T> serviceType, Enum key) {
        for (ServiceDefinition<?> definition : serviceDefinitions) {
            if (definition.getService().equals(serviceType) && definition.getKey().equals(key)) {
                //noinspection unchecked
                return (ServiceDefinition<T>) definition;
            }
        }

        return null;
    }

    protected <T> ServiceDefinitionBuilder<T> addService(Class<T> serviceType) {
        ServiceDefinitionBuilder<T> builder = new ServiceDefinitionBuilder<T>(serviceType);
        configuredBuilders.add(builder);
        return builder;
    }

    void initialize() {
        configure();
        applyConfiguration();
    }

    protected void applyConfiguration() {
        for (ServiceDefinitionBuilder<?> builder : configuredBuilders) {
            serviceDefinitions.add(builder.build());
        }
    }

    protected abstract void configure();
}
