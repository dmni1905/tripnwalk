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

    //  указание пути к фалам *html
    //  необходимо чтобы работал host swapping
    @Bean
    public ITemplateResolver defaultTemplateResolver() {
        TemplateResolver resolver = new FileTemplateResolver();
        resolver.setSuffix(".html");
        resolver.setPrefix("src/main/webapp/WEB-INF/view/");
        resolver.setTemplateMode("HTML5");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        return resolver;
    }

    //  указание пути к фалам *js и css
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(new Object[]{Application.class, ServletContainerCustomizer.class}, args);
    }

}
