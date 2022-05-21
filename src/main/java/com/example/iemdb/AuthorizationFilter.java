package com.example.iemdb;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.ArrayList;

@Component
@Order(1)
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            ArrayList<String> urlsWithoutTokens = new ArrayList<>();
            urlsWithoutTokens.add("http://127.0.0.1:8080/users/login");
            urlsWithoutTokens.add("http://127.0.0.1:8080/users/logout");
            urlsWithoutTokens.add("http://127.0.0.1:8080/users/signup");
            urlsWithoutTokens.add("http://127.0.0.1:8080/users/callback");

            if (!urlsWithoutTokens.contains(httpServletRequest.getRequestURL().toString())) {
                String jwtString = httpServletRequest.getHeader("Authorization");

                System.out.println(jwtString);

                String SECRET_KEY = "iemdb1401";

                byte[] keyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY+SECRET_KEY+SECRET_KEY+SECRET_KEY+SECRET_KEY);
                Key key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());

                Jws<Claims> jwt = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwtString);

                System.out.println(jwt);
            }

            chain.doFilter(httpServletRequest, httpServletResponse);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
