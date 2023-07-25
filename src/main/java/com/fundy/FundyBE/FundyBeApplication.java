package com.fundy.FundyBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FundyBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundyBeApplication.class, args);
	}
	//TODO: 메일 인증 서비스 구현
	//TODO: Oauth2 구현
	//TODO: Refresh Token 사용
}
