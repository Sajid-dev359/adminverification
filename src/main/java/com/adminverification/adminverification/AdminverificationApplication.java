package com.adminverification.adminverification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableTransactionManagement
public class AdminverificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminverificationApplication.class, args);
	}
	@Configuration
	public class CorsConfig implements WebMvcConfigurer {

		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**")
					.allowedOrigins("http://localhost:3000") // Allow requests from this origin
					.allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
					.allowedHeaders("*")
					.allowCredentials(true);
		}
	}


}
