package com.hello.spring;


import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/* 
 * This class further configures Spring MVC. The annotations below direct Spring where to scan for annotated classes and methods
 * and provides utility configuration not handled elsewhere.
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com.hello")
public class SpringWebMvcConfig extends WebMvcConfigurerAdapter {

	
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureMessageConverters(java.util.List)
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
		/****jdk8 support ***/
		
//		 Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
//         .indentOutput(true)
//         .dateFormat(new SimpleDateFormat("yyyy-MM-dd"))
//         .modulesToInstall(new ParameterNamesModule());
// converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
// converters.add(new MappingJackson2XmlHttpMessageConverter(builder.xml().build()));
		/*****************************/
//		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder().indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		converters.add(new MappingJackson2HttpMessageConverter());
//		converters.add(new MappingJackson2XmlHttpMessageConverter());//xml
		super.configureMessageConverters(converters);
	}

	//Tell Spring that we want to serve static content and it should look for
	//these resources in the root of the jar in the various directories listed
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
    }
    
    
    
    

}
