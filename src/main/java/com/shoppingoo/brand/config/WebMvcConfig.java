//package com.shoppingoo.brand.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    private List<String> DEFAULT_EXCLUDE = List.of(
//            "/",
//            "favicon.ico",
//            "/error"
//    );
//
//    private List<String> SWAGGER = List.of(
//            "/swagger-ui.html",
//            "/swagger-ui/**",
//            "/v3/api-docs/**"
//    );
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // /uploads/** URL을 static/uploads 디렉토리로 매핑
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations("classpath:/uploads/")  // resources/static/uploads 경로로 매핑
//                .setCachePeriod(3600); // 캐시 기간 설정 (초 단위)
//    }
//
//
//    @Bean
//    public WebFilter removeDuplicateVaryHeaders() {
//        return (exchange, chain) -> {
//            exchange.getResponse().getHeaders().remove("Vary");
//            return chain.filter(exchange);
//        };
//    }
//}
