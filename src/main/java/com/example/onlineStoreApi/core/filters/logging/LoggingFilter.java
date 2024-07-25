package com.example.onlineStoreApi.core.filters.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;


//@Component
//@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        logger.info("Logging Request  {} : {}\n", req.getMethod(), req.getRequestURI());
        chain.doFilter(request, response);
        logger.info("Logging Response : {}\n", res.getContentType());
    }
}
