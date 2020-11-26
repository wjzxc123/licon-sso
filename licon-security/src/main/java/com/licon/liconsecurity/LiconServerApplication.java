package com.licon.liconsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
