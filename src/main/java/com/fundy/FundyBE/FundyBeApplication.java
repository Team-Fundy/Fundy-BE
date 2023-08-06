package com.fundy.FundyBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FundyBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundyBeApplication.class, args);
	}
	//TODO: Oauth2 구현
	//TODO: S3 이미지 업로더
	//TODO: 체계적 테스트 구현
	//TODO: 서버 자체 문서화
	//TODO: 일반 / 크리에이터 유저 회원가입 구분
}
