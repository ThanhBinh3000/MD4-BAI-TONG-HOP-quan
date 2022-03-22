package com.codegym.Config;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@EnableWebMvc
@Configuration
@ComponentScan("com.codegym")
@PropertySource("classpath:upload-file.properties")
public class Appconfig implements WebMvcConfigurer, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Value("${file-upload}")
    private String uploadPath;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver(); //SpringResourceTemplateResolver => class con của interface TemplateResolver
        templateResolver.setPrefix("/WEB-INF/views/"); //tiền tố
        templateResolver.setSuffix(".html"); //Đuổi file
        templateResolver.setTemplateMode(TemplateMode.HTML); //Định dạng của view
        templateResolver.setCharacterEncoding("UTF-8"); // Viết tiếng việt
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() { //Template engine được khai báo để sử dụng bộ khung template resolver ở trên
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() { //Sử dụng bộ template engine đã khai báo
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Override phương thức add resource handler để cấu hình đường dẫn file tĩnh
        //=> để lưu trữ các file ảnh, mp3, mp4 khi được upload lên server
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:" + uploadPath); //Để khai báo cấu hình lữu trữ trên server
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSizePerFile(52428800);//Set dung lượng tối đa khi upload => 5MB // Không giới hạn thì để -1
        return multipartResolver;
    }
}
