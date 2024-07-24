package com.example.onlineStoreApi.core.filters;

import com.example.onlineStoreApi.core.filters.logging.LoggingFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
@AllArgsConstructor
public class FilterConfig {


    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilter() {
        FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoggingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); // Set the order
        return registrationBean;
    }

//    @Bean
//    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter() {
//        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new JwtAuthenticationFilter(jwtService, userDetailsService));
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.setOrder(2); // Set the order
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean<AnotherLoggingFilter> anotherLoggingFilter() {
//        FilterRegistrationBean<AnotherLoggingFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new AnotherLoggingFilter());
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.setOrder(3); // Set the order
//        return registrationBean;
//    }
}
