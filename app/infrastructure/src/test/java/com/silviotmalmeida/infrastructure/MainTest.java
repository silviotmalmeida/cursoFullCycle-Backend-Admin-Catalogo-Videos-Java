package com.silviotmalmeida.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.AbstractEnvironment;

public class MainTest {

    @Test
    public void testMain() {

        // utilizando as configurações do profile test-integration, se quiser usar o h2
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test-integration");
        // utilizando as configurações do profile development, se quiser usar o mysql
//        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "development");

        Assertions.assertNotNull(new Main());
        Main.main(new String[]{});
    }
}
