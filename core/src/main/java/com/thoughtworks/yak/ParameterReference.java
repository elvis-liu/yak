package com.thoughtworks.yak;

public class ParameterReference <T> {
    private T value;
    private Enum serviceKey;
    private Class<T> service;

    private static enum ReferenceType {
        SERVICE,
        VALUE;
    };
    private final ReferenceType refType;

    private ParameterReference(T value) {
        refType = ReferenceType.VALUE;
        this.value = value;
    }

    private ParameterReference(Class<T> service, Enum key) {
        refType = ReferenceType.SERVICE;
        this.serviceKey = key;
        this.service = service;
    }

    public T dereference(ServiceContainer container) {
        if (refType == ReferenceType.SERVICE) {
            return container.getService(service, serviceKey);
        } else {
            return value;
        }
    }

    public T getValue() {
        return value;
    }

    public Enum getServiceKey() {
        return serviceKey;
    }

    public Class<T> getService() {
        return service;
    }

    public ReferenceType getRefType() {
        return refType;
    }

    public static <T> ParameterReference<T> refByService(Class<T> service) {
        return new ParameterReference<T>(service, null);
    }

    public static <T> ParameterReference<T> refByService(Class<T> service, Enum key) {
        return new ParameterReference<T>(service, key);
    }

    public static <T> ParameterReference<T> refByValue(T value) {
        return new ParameterReference<T>(value);
    }
}
