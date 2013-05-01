package com.thoughtworks.yak;

public class ServiceDefinition <T> {
    private final Class<T> service;
    private final Class<? extends T> serviceProvider;
    private final Enum key;

    ServiceDefinition(Class<T> service, Class<? extends T> serviceProvider, Enum key) {
        this.service = service;
        this.serviceProvider = serviceProvider;
        this.key = key;
    }

    public Class<? extends T> getServiceProvider() {
        return serviceProvider;
    }

    public Class<T> getService() {
        return service;
    }

    public Enum getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceDefinition that = (ServiceDefinition) o;

        if (!service.equals(that.service)) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        //noinspection RedundantIfStatement
        if (!serviceProvider.equals(that.serviceProvider)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = service.hashCode();
        result = 31 * result + serviceProvider.hashCode();
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }
}
