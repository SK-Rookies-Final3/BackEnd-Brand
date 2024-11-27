package com.shoppingoo.brand.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebFilter;

@Configuration
public class WebfluxConfig implements WebFluxConfigurer {

//    // 배포시 변경
//        @Override
//        public void addCorsMappings(CorsRegistry registry) {
//            registry.addMapping("/**")
//                    .allowedOrigins("http://localhost:*")
//                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                    .allowedHeaders("*")
//                    .allowCredentials(true);
//        }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploads/** URL을 로컬 uploads 디렉토리로 매핑
        System.out.println("path:::"+ System.getProperty("user.dir"));

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:"+System.getProperty("user.dir")+"/uploads/")  ;// 웹 서버의 uploads 디렉토리 경로

    }

        @Bean
        public WebFilter removeDuplicateVaryHeaders() {
            return (exchange, chain) -> {
                exchange.getResponse().getHeaders().remove("Vary");
                return chain.filter(exchange);
            };
        }
    }



