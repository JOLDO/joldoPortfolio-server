package com.portfolio_server.config;

import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Serves uploaded images from the local upload directory. CORS/auth live in SecurityConfig. */
@Configuration
public class WebConfig implements WebMvcConfigurer {    //WebMvcConfigurer는 웹 관련 설정을 커스터마이징 했고 security에서는 web.ignore로 security 무시함

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.upload.public-path:/uploads}")
    private String publicPathPrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { //정적 리소스를 어떤 URL로 요청오면 어떤 파일을 보내줄지 정함
        String location = Paths.get(uploadDir).toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler(publicPathPrefix + "/**")
                .addResourceLocations(location);
    }
}