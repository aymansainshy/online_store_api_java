package com.example.onlineStoreApi.core.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(urlPatterns = "/api/v1/users")  // But note: @WebFilter requires enabling @ServletComponentScan in your main
public class UserGetFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Filter only for /orders/*" + request.getRemoteAddr());
        chain.doFilter(request, response); // ✅ like next()
    }
}
