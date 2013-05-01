package com.thoughtworks.yak;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServiceDefinitionBuilderTest {
    @Test
    public void shouldBuildWithServiceAsInterface() throws Exception {
        ServiceDefinition serviceDefinition = new ServiceDefinitionBuilder<TestInterfaceService>(TestInterfaceService.class).implementedBy(TestServiceProvider.class).build();

        assertEquals(serviceDefinition.getService(), TestInterfaceService.class);
        assertEquals(serviceDefinition.getServiceProvider(), TestServiceProvider.class);
    }

    @Test
    public void shouldBuildWithServiceAsAbstractClass() throws Exception {
        ServiceDefinition serviceDefinition = new ServiceDefinitionBuilder<TestAbstractClassService>(TestAbstractClassService.class).implementedBy(TestServiceProvider.class).build();

        assertEquals(serviceDefinition.getService(), TestAbstractClassService.class);
        assertEquals(serviceDefinition.getServiceProvider(), TestServiceProvider.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfServiceIsNonAbstractClass() throws Exception {
        new ServiceDefinitionBuilder<TestServiceProvider>(TestServiceProvider.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfServiceProviderIsAbstractClass() throws Exception {
        new ServiceDefinitionBuilder<TestAbstractClassService>(TestAbstractClassService.class).implementedBy(TestAbstractClassService.class).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfServiceProviderIsInterface() throws Exception {
        new ServiceDefinitionBuilder<TestInterfaceService>(TestInterfaceService.class).implementedBy(TestInterfaceService.class).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfServiceProviderIsNotPublic() throws Exception {
        new ServiceDefinitionBuilder<TestInterfaceService>(TestInterfaceService.class).implementedBy(PrivateClass.class).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfServiceProviderNonStaticInnerClass() throws Exception {
        new ServiceDefinitionBuilder<TestInterfaceService>(TestInterfaceService.class).implementedBy(NonStaticInnerClass.class).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfServiceProviderDoesNOTHavePublicConstructor() throws Exception {
        new ServiceDefinitionBuilder<TestInterfaceService>(TestInterfaceService.class).implementedBy(NoPublicConstructor.class).build();
    }

    public interface TestInterfaceService {
    }

    public static class TestServiceProvider extends TestAbstractClassService implements TestInterfaceService {
    }

    public static abstract class TestAbstractClassService {
    }

    private static class PrivateClass implements TestInterfaceService {
    }

    public class NonStaticInnerClass implements TestInterfaceService {
    }

    public static class NoPublicConstructor implements TestInterfaceService {
        private NoPublicConstructor() {}
    }
}
