package com.songsy.iframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author songshuiyang
 * @date 2018/10/28 9:31
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@MapperScan("com.songsy.iframe.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
