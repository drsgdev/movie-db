package com.github.drsgdev.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/add/**").allowedOrigins("*").maxAge(3600l).allowedHeaders("*")
        .allowCredentials(true);
    registry.addMapping("/movie/**").allowedOrigins("*").maxAge(3600l).allowedHeaders("*")
        .allowCredentials(true);
    registry.addMapping("/show/**").allowedOrigins("*").maxAge(3600l).allowedHeaders("*")
        .allowCredentials(true);
    registry.addMapping("/person/**").allowedOrigins("*").maxAge(3600l).allowedHeaders("*")
        .allowCredentials(true);
    registry.addMapping("/find/**").allowedOrigins("*").maxAge(3600l).allowedHeaders("*")
        .allowCredentials(true);
    registry.addMapping("/api/auth/**").allowedOrigins("*").maxAge(3600l).allowedHeaders("*")
        .allowCredentials(true);

    WebMvcConfigurer.super.addCorsMappings(registry);
  }
}
