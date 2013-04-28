package com.thoughtworks.yak;

public class ServiceDefinition <T> {
    private final Class<T> service;
    private final Class<? extends T> serviceProvider;

    ServiceDefinition(Class<T> service, Class<? extends T> serviceProvider) {
        this.service = service;
        this.serviceProvider = serviceProvider;
    }

    public Class<? extends T> getServiceProvider() {
        return serviceProvider;
    }

    public Class<T> getService() {
        return service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceDefinition that = (ServiceDefinition) o;

        if (!serviceProvider.equals(that.serviceProvider)) return false;
        if (!service.equals(that.service)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = service.hashCode();
        result = 31 * result + serviceProvider.hashCode();
        return result;
    }
}
