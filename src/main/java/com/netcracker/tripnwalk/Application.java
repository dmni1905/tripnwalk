package com.netcracker.tripnwalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class Application extends WebMvcConfigurerAdapter {
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> container.setPort(9095);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bower_components/**").addResourceLocations("/bower_components/");
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("/view/templates/");
    }

    @Bean
    public ITemplateResolver defaultTemplateResolver() {
        TemplateResolver resolver = new FileTemplateResolver();
        resolver.setSuffix(".html");
        resolver.setPrefix("src/main/webapp/view/");
        resolver.setTemplateMode("HTML5");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        return resolver;
    }

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(new Object[]{Application.class}, args);
    }
}
