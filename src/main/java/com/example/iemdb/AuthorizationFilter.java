package com.example.iemdb;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import main.User;
import manager.AuthenticationManager;
import manager.UserManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

@Component
@Order(2)
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        try {

            ArrayList<String> urlsWithoutTokens = new ArrayList<>();
            urlsWithoutTokens.add("http://127.0.0.1:8080/users/login");
            urlsWithoutTokens.add("http://127.0.0.1:8080/users/logout");
            urlsWithoutTokens.add("http://127.0.0.1:8080/users/signup");
            urlsWithoutTokens.add("http://127.0.0.1:8080/callback/");
            if (!urlsWithoutTokens.contains(httpServletRequest.getRequestURL().toString())) {
                String jwtString = httpServletRequest.getHeader("Authorization");
                Claims claims = AuthenticationManager.parseJWT(jwtString);
                httpServletRequest.setAttribute("user", UserManager.getUser(claims.get("email").toString()));
            }

            chain.doFilter(httpServletRequest, httpServletResponse);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
