package com.fundy.FundyBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FundyBeApplication {
	// TODO: 인증된 크리에이터 / 인증 안된 크리에이터 구분 -> 인증 안되면 홍보용 프로젝트만 가능
	public static void main(String[] args) {
		SpringApplication.run(FundyBeApplication.class, args);
	}
}
