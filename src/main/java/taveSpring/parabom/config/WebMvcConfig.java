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
        registry.addResourceHandler("/resources/**") // 정적자원이 참조하는 링크
                .addResourceLocations(uploadPath); // file:///실제경로
    }

}