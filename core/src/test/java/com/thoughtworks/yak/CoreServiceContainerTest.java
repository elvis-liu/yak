package com.thoughtworks.yak;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class CoreServiceContainerTest {

    private enum SERVICE_KEYS {
        TEST_SERVICE_A
    }

    @Test
    public void shouldBuildContainerWithService() throws Exception {
        ServiceModule serviceModule = new ServiceModule() {
            @Override
            protected void configure() {
                addService(TestService.class).implementedBy(TestServiceProvider.class);
            }
        };
        ServiceContainer serviceContainer = new CoreServiceContainer(serviceModule);

        TestService service = serviceContainer.getService(TestService.class);
        assertThat(service, notNullValue());
    }

    @Test
    public void shouldConstructorWithConstructorWithMostParameters() throws Exception {
        ServiceModule serviceModule = new ServiceModule() {
            @Override
            protected void configure() {
                addService(List.class).implementedBy(ArrayList.class);
                addService(Set.class).implementedBy(HashSet.class);

                addService(TestService.class).implementedBy(TestServiceProvider.class);
            }
        };
        ServiceContainer serviceContainer = new CoreServiceContainer(serviceModule);

        TestService service = serviceContainer.getService(TestService.class);
        assertThat(service.getTestList(), notNullValue());
        assertThat(service.getTestSet(), notNullValue());
    }

    @Test
    public void shouldConstructorWithConstructorWithSuitableParameters() throws Exception {
        ServiceModule serviceModule = new ServiceModule() {
            @Override
            protected void configure() {
                addService(List.class).implementedBy(ArrayList.class);

                addService(TestService.class).implementedBy(TestServiceProvider.class);
            }
        };
        ServiceContainer serviceContainer = new CoreServiceContainer(serviceModule);

        TestService service = serviceContainer.getService(TestService.class);
        assertThat(service.getTestList(), notNullValue());
        assertThat(service.getTestSet(), nullValue());
    }

    @Test
    public void shouldGetServiceWithKey() throws Exception {
        ServiceModule serviceModule = new ServiceModule() {
            @Override
            protected void configure() {
                addService(TestService.class).withKey(SERVICE_KEYS.TEST_SERVICE_A).implementedBy(TestServiceProvider.class);
            }
        };
        ServiceContainer serviceContainer = new CoreServiceContainer(serviceModule);

        assertThat(serviceContainer.getService(TestService.class), nullValue());
        assertThat(serviceContainer.getService(TestService.class, SERVICE_KEYS.TEST_SERVICE_A), notNullValue());
    }

    public interface TestService {
        List getTestList();
        Set getTestSet();
    }

    @SuppressWarnings("unused")
    public static class TestServiceProvider implements TestService {
        private final List testList;
        private final Set testSet;

        public TestServiceProvider(List testList, Set testSet) {
            this.testList = testList;
            this.testSet = testSet;
        }

        public TestServiceProvider(List testList) {
            this.testList = testList;
            this.testSet = null;
        }

        public TestServiceProvider(Set testSet) {
            this.testList = null;
            this.testSet = testSet;
        }

        public TestServiceProvider() {
            this.testList = null;
            this.testSet = null;
        }

        @Override
        public List getTestList() {
            return testList;
        }

        @Override
        public Set getTestSet() {
            return testSet;
        }
    }
}
