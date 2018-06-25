package com.example.demo;

import com.example.demo.filter.EncodingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

@SpringBootApplication
@ServletComponentScan
public class ClassPracticeApplication {
	@Bean
	public FilterRegistrationBean encodingFilterRegistration() {

		FilterRegistrationBean filterRegistrationBean= new FilterRegistrationBean();
		filterRegistrationBean.setFilter(encodingFilter());
		filterRegistrationBean.setName("myEn");
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("encoder","utf-8");
		filterRegistrationBean.setOrder(1);
		return filterRegistrationBean;
	}
	@Bean(name="myEn")
	public Filter encodingFilter(){
		return new EncodingFilter();
	}

	public static void main(String[] args) {

		SpringApplication.run(ClassPracticeApplication.class, args);
	}
}
