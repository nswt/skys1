package com.sky.app;

import java.util.Properties;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * @author Simon
 * @date 2018-01-20
 */
@ComponentScan(basePackages = { "com.sky" })
//设置启动时spring能够扫描到我们自己编写的servlet和filter, 用于Druid监控
@ServletComponentScan 
//扫描的是mapper.xml中namespace指向值的包位置
@MapperScan("com.sky.**.dao")   
@SpringBootApplication
public class SkysApplication {
	public static void main(String[] args) {
		SpringApplication.run(SkysApplication.class, args);
	}

	@Bean
	public static MapperScannerConfigurer mapperScannerConfigurer() { 
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
		//与@MapperScan定义重复了，此处只能随便写一个
		mapperScannerConfigurer.setBasePackage("com.sky.**.mapper");
		// 配置通用mappers
		Properties properties = new Properties();
		properties.setProperty("mappers", "tk.mybatis.mapper.common.Mapper");
		properties.setProperty("notEmpty", "false");
		// 主键自增回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)
		//@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select uuid()")
		properties.setProperty("IDENTITY", "select uuid()");
		properties.setProperty("ORDER", "BEFORE");
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
