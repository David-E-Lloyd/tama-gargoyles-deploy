package com.example.tama_gargoyles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class WebConfig {

    // This bean forces Spring to read the "X-Forwarded-Proto" header
    // effectively fixing the "Redirect to HTTP" bug on Render.
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}