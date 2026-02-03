package com.atun.brain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.atun.brain.infrastructure.persistence.mybatis.mapper")
public class App 
{
    public static void main( String[] args ) {
        SpringApplication app = new SpringApplication(App.class);
        app.run(args);
    }
}
