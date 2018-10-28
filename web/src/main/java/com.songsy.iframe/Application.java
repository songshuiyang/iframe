package com.songsy.iframe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author songshuiyang
 * @date 2018/10/28 9:31
 */
@SpringBootApplication
@MapperScan("com.songsy.iframe.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
