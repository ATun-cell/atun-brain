package com.atun.brain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 阿吞智能记账助手 - 启动类
 *
 */
@SpringBootApplication
@EnableAsync
public class App
{
    public static void main( String[] args ) {
        SpringApplication app = new SpringApplication(App.class);
        app.run(args);
    }
}
