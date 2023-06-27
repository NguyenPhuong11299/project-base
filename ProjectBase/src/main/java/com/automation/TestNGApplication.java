package com.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan()
public class TestNGApplication {
    public static void main(String[] args){
        //https://anonyviet.com/cach-chay-chuong-trinh-java-trong-ubuntu/
        System.out.println("+++++++++++++++++++++++++ testNGApplication");
        SpringApplication.run(TestNGApplication.class, args);
    }
}
