package com.thoughtworks.yak;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CoreDeanContainerTest {

    private enum RESOURCE_KEYS {
        TEST_RESOURCE;
    };

    @Test
    public void shouldBuildContainerWithService() throws Exception {
        ServiceModule serviceModule = new ServiceModule() {
            @Override
            protected void configure() {
                addService(TestService.class).implementedBy(TestServiceProvider.class);
            }
        };
        DeanContainer deanContainer = new CoreDeanContainer(serviceModule);

        TestService service = deanContainer.getService(TestService.class);
        assertThat(service, notNullValue());
    }

    public interface TestService {
    }

    public static class TestServiceProvider implements TestService {
    }
}
