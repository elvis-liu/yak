package com.thoughtworks.yak.injectors;

import com.thoughtworks.yak.ParameterReference;
import com.thoughtworks.yak.ServiceContainer;
import com.thoughtworks.yak.ServiceDefinition;
import com.thoughtworks.yak.ServiceFactory;
import com.thoughtworks.yak.exceptions.ServiceInstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

import static com.thoughtworks.yak.ParameterReference.refByService;

public class ConstructorInjector <T> implements ServiceFactory<T> {
    @Override
    public T createService(ServiceContainer container, ServiceDefinition<T> serviceDefinition) {
        ConstructChoice constructChoice = chooseConstructor(container, serviceDefinition);
        Constructor<? extends T> constructor = constructChoice.constructor;
        Object[] parameters = constructChoice.getParameters(container);
        try {
            return (T) constructor.newInstance(parameters);
        } catch (InstantiationException e) {
            throw new ServiceInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ServiceInstantiationException(e);
        } catch (InvocationTargetException e) {
            throw new ServiceInstantiationException(e);
        }
    }

    // Choose the constructor with most parameters we can resolve
    private ConstructChoice<? extends T> chooseConstructor(ServiceContainer container, ServiceDefinition<T> serviceDefinition) {
        Class<? extends T> targetClass = serviceDefinition.getServiceProvider();
        Constructor<? extends T>[] constructors = getSortedSuitableConstructors(targetClass);
        Constructor<? extends T> selectedConstructor = null;
        ParameterReference<?>[] parameterReferences = null;
        int bestResolveCount = -1;

        for (Constructor<? extends T> constructor : constructors) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            ParameterReference<?>[] paramRefs = convertParamTypesToRefs(paramTypes);
            if (canResolveTypes(container, paramRefs)) {
                if (paramRefs.length > bestResolveCount) {
                    selectedConstructor = constructor;
                    parameterReferences = paramRefs;
                    bestResolveCount = paramRefs.length;
                }
            }
        }

        if (selectedConstructor == null || parameterReferences == null) {
            throw new ServiceInstantiationException("No suitable constructor to use!");
        }

        return new ConstructChoice(selectedConstructor, parameterReferences);
    }

    private Constructor<? extends T>[] getSortedSuitableConstructors(Class<? extends T> targetClass) {
        Constructor<? extends T>[] constructors = (Constructor<? extends T>[]) targetClass.getConstructors();
        Arrays.sort(constructors, new Comparator<Constructor<? extends T>>() {
            @Override
            public int compare(Constructor<? extends T> constructor1, Constructor<? extends T> constructor2) {
                return constructor2.getParameterTypes().length - constructor1.getParameterTypes().length;
            }
        });

        return constructors;
    }

    private ParameterReference<?>[] convertParamTypesToRefs(Class<?>[] paramTypes) {
        ParameterReference<?>[] paramRefs = new ParameterReference[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramRefs[i] = refByService(paramTypes[i]);
        }
        return paramRefs;
    }

    private boolean canResolveTypes(ServiceContainer container, ParameterReference<?>[] paramRefs) {
        for (ParameterReference<?> ref : paramRefs) {
            if (!container.providesService(ref.getService(), ref.getServiceKey())) {
                return false;
            }
        }

        return true;
    }

    private static class ConstructChoice <D> {
        private Constructor<D> constructor;
        private ParameterReference<?>[] parameterReferences;

        private ConstructChoice(Constructor<D> constructor, ParameterReference<?>[] parameterReferences) {
            this.constructor = constructor;
            this.parameterReferences = parameterReferences;
        }

        public Object[] getParameters(ServiceContainer container) {
            Object[] params = new Object[parameterReferences.length];
            for (int i = 0; i < parameterReferences.length; i++) {
                params[i] = parameterReferences[i].dereference(container);
            }

            return params;
        }
    }
}
