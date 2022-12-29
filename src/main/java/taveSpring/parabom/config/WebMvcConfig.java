package taveSpring.parabom.config;

import lombok.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //@Value("${custom.path.uploadPath}")
    String uploadPath = "D:\\22-12-Parabom-Project-new";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("localhost:8080/post/productDetail/**") // 리소스와 연결될 URL path (클라이언트가 파일에 접근하기 위해 요청하는 url)
                .addResourceLocations(uploadPath); // file:///실제로 파일이 존재하는 경로
    }

}