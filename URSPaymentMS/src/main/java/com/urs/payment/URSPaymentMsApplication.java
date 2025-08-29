package com.urs.payment;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
public class URSPaymentMsApplication {
    public static void main(String[] args) {
        SpringApplication.run(URSPaymentMsApplication.class, args);
    }
    
    @Bean
    public ModelMapper modelMapper() {
    	return new ModelMapper();
    }
    
    @Bean
	@LoadBalanced
	public RestTemplate getTemplate() {
		return new RestTemplate();
    }
    
}
