package com.cuikeyao.gitbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author KEYAO
 */
@EnableScheduling
@SpringBootApplication
public class GitBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitBotApplication.class, args);
    }
}
