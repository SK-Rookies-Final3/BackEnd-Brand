package com.shoppingoo.brand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableDiscoveryClient
@SpringBootApplication
public class BrandApplication {
	public static void main(String[] args) {
		SpringApplication.run(BrandApplication.class, args);
	}
}