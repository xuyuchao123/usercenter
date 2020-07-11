package com.xyc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.xyc.userc.dao")
@EnableTransactionManagement
public class UsercenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsercenterApplication.class, args);
	}

}
