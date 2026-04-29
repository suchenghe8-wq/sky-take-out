package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.List;

@Configuration
@Slf4j
@Data
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login")
                // 以下是Knife4j/Swagger的静态资源放行
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/v2/api-docs/**");
    }

    /**
     * 通过knife4j生成接口文档
     */
    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                // 修改点：加上【管理端】前缀
                .title("苍穹外卖项目接口文档 - 管理端")
                .version("2.0")
                .description("苍穹外卖项目接口文档 - 管理端")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }



    /**
     * 设置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始设置静态资源映射...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展Spring MVC框架的消息转换器
     */
   /* @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapper());
        // 将自定义的转换器添加到集合的第0位，使其优先被使用
        converters.add(0, converter);
    }*/

    /**
     * 扩展Spring MVC框架的消息转换器
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("开始扩展消息转换器...");

        // 1. 创建我们自定义的消息转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        // 2. 设置对象映射器（就是刚才那个 JacksonObjectMapper）
        converter.setObjectMapper(new JacksonObjectMapper());

        // 3. 【关键步骤】将自定义转换器加到列表的最前面
        // 这样 Spring Boot 会优先使用你的转换器处理时间格式
        // 而不会去清空列表，保留了 Knife4j 需要的其他默认转换器
        converters.add(0, converter);

        log.info("消息转换器扩展完成。");
    }
}