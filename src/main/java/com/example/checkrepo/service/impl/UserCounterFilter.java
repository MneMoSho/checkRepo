package com.example.checkrepo.service.impl;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class UserCounterFilter implements Filter {
    private VisitCounterService visitCounterService;
    public UserCounterFilter(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestUri = httpRequest.getRequestURI();
        System.out.println(requestUri);
        if (requestUri.startsWith("/api/users/")) {
            visitCounterService.incrementCount(requestUri);
        }
        chain.doFilter(request, response);
    }
}

