package com.gracetech.jobtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import com.gracetech.jobtest.logging.LoggingAspect;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

	@Bean
    @Profile("dev")
    public LoggingAspect loggingAspect(final Environment env) {
        return new LoggingAspect(env);
    }
}
