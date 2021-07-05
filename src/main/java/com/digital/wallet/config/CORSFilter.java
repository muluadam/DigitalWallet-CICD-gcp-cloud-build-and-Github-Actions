/*
 * package com.digital.wallet.config;
 * 
 * import org.springframework.beans.factory.annotation.Value; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.web.servlet.config.annotation.CorsRegistry; import
 * org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 * 
 * @Configuration
 * 
 * public class CORSFilter implements WebMvcConfigurer {
 * 
 * @Value("${allowed.origins}") private String[] theAllowedOrigins;
 * 
 * @Override public void addCorsMappings(CorsRegistry cors) {
 * 
 * // set up cors mapping
 * cors.addMapping("/**").allowedOrigins(theAllowedOrigins);
 * 
 * } }
 */