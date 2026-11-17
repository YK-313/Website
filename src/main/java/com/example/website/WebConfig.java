package com.example.website;

import org.springframework.lang.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // "/uploads/**" というURLへのリクエストが来た時に、
        // "file:uploads/" というプロジェクトルート直下の物理的なフォルダを探しに行くよう設定します。
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
