package com.atun.brain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.atun.brain.infrastructure.persistence.mybatis.mapper")
@EnableAsync
public class App 
{
    public static void main( String[] args ) {
        SpringApplication app = new SpringApplication(App.class);
        app.run(args);
    }
}
