package com.lds;

import com.lds.config.AsyncSyncConfiguration;
import com.lds.config.EmbeddedSQL;
import com.lds.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        LegalApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        com.lds.config.JacksonHibernateConfiguration.class,
    }
)
@EmbeddedSQL
public @interface IntegrationTest {}
