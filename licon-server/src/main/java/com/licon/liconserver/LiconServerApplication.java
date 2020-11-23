package com.licon.liconserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//@EnableAuthorizationServer
@SpringBootApplication
public class LiconServerApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(LiconServerApplication.class, args);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
