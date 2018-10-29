package com.sky.app;

import java.util.Properties;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * @author Simon
 * @date 2018-01-20
 */
@ComponentScan(basePackages = { "com.sky" })
@MapperScan("com.sky.**.dao") 
@SpringBootApplication
public class SshsApplication {
	public static void main(String[] args) {
		SpringApplication.run(SshsApplication.class, args);
	}

	@Bean
	public static MapperScannerConfigurer mapperScannerConfigurer() { 
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
		mapperScannerConfigurer.setBasePackage("com.sky.*.*.dao");
		// 配置通用mappers
		Properties properties = new Properties();
		properties.setProperty("mappers", "tk.mybatis.mapper.common.Mapper");
		properties.setProperty("notEmpty", "false");
		mapperScannerConfigurer.setProperties(properties);

		return mapperScannerConfigurer;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
		c.setIgnoreUnresolvablePlaceholders(true);
		return c;
	}
}
