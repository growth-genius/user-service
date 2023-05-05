package com.sgyj.userservice.configuration;

import com.sgyj.userservice.interceptor.RequestLogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    private final RequestLogInterceptor requestLogInterceptor;

    @Override
    public void addInterceptors ( InterceptorRegistry registry ) {
        registry.addInterceptor( requestLogInterceptor );
    }


}
