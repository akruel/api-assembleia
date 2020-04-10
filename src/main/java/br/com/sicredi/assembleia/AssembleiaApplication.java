package br.com.sicredi.assembleia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Trello do Projeto: https://trello.com/b/LLuq5hxp/api-assembleia
 */

@EnableSwagger2
@EnableFeignClients

@SpringBootApplication
public class AssembleiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssembleiaApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
