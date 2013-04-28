package com.thoughtworks.yak.injectors;

import com.thoughtworks.yak.DeanContainer;
import com.thoughtworks.yak.ParameterReference;
import com.thoughtworks.yak.ServiceDefinition;
import com.thoughtworks.yak.ServiceFactory;
import com.thoughtworks.yak.exceptions.ServiceInstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInjector <T> implements ServiceFactory<T> {
    @Override
    public T createService(DeanContainer container, ServiceDefinition<T> serviceDefinition) {
        Class<? extends T> targetClass = serviceDefinition.getServiceProvider();
        ConstructChoice constructChoice = chooseConstructor(container, targetClass);
        try {
            return (T) constructChoice.constructor.newInstance(constructChoice.getParameters(container));
        } catch (InstantiationException e) {
            throw new ServiceInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ServiceInstantiationException(e);
        } catch (InvocationTargetException e) {
            throw new ServiceInstantiationException(e);
        }
    }

    private ConstructChoice chooseConstructor(DeanContainer container, Class<? extends T> targetClass) {
        Constructor<?>[] constructors = targetClass.getConstructors();
        return new ConstructChoice(constructors[0], new ParameterReference[0]);
    }

    private static class ConstructChoice {
        private Constructor<?> constructor;
        private ParameterReference[] parameterReferences;

        private ConstructChoice(Constructor<?> constructor, ParameterReference[] parameterReferences) {
            this.constructor = constructor;
            this.parameterReferences = parameterReferences;
        }

        public Object[] getParameters(DeanContainer container) {
            Object[] params = new Object[parameterReferences.length];
            for (int i = 0; i < parameterReferences.length; i++) {
                params[i] = parameterReferences[i].dereference(container);
            }

            return params;
        }
    }
}
