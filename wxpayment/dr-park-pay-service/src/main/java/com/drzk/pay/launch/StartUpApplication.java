package com.drzk.pay.launch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.drzk.pay.aop",
		"com.drzk.pay.core",
        "com.drzk.pay.dto",
        "com.drzk.pay.i18n",
        "com.drzk.pay.service",
        "com.drzk.pay.service.Impl",
        "com.drzk.pay.aspect",
        "com.drzk.pay.controller",
        "com.drzk.pay.mqtt"})
@ServletComponentScan("com.drzk.pay.controller.*")
@IntegrationComponentScan("com.drzk.pay.mqtt")
@MapperScan("com.drzk.pay.mapper")
@EnableTransactionManagement
@EnableSwagger2
@EnableCaching
@EnableAutoConfiguration
public class StartUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartUpApplication.class, args);
	}
}
