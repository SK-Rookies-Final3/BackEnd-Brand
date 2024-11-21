package com.shoppingoo.brand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EnableDiscoveryClient
public class BrandApplication {
	public static void main(String[] args) {
		SpringApplication.run(BrandApplication.class, args);
	}
}