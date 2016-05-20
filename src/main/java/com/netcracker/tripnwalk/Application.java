package com.netcracker.tripnwalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class Application{
    public static void main(String[] args) throws Throwable {
        SpringApplication.run(new Object[]{Application.class}, args);
    }
}
