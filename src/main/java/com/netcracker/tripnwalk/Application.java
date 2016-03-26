package com.netcracker.tripnwalk;

import com.netcracker.tripnwalk.server.ServletContainerCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import javax.sql.DataSource;

@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {
    public static void main(String[] args) throws Throwable {
        SpringApplication.run(new Object[]{Application.class, ServletContainerCustomizer.class}, args);
    }
}
