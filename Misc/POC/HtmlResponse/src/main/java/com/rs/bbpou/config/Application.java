package com.rs.bbpou.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@ComponentScan{("com.rs.bbpou.config","com.rs.bbpou.controller","com.rs.bbpou.dao","com.rs.bbpou.model","com.rs.bbpou.service")}
@EnableWebMvc 
@ComponentScan({"com.rs.bbpou.config","com.rs.bbpou.controller","com.rs.bbpou.dao","com.rs.bbpou.service","com.rs.bbpou.model"})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
