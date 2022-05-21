package com.example.iemdb;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig fc) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        ((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) response).setHeader("Access-Control-Allow-Credentials", "true");
        ((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "*");
        ((HttpServletResponse) response).setHeader("Access-Control-Expose-Headers", "*");
        if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {

            ((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "*");
            ((HttpServletResponse) response).setHeader("Access-Control-Max-Age", "1800");
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_ACCEPTED);
        } else
            chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}