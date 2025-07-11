package com.company.projectjwt1.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyCustomEntryPoint implements AuthenticationEntryPoint {

    private final Logger log = LoggerFactory.getLogger(MyCustomEntryPoint.class);

    @Override

    /** This commence method execute when any Exception comes/throw during Authentication process */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        PrintWriter writer = response.getWriter();
//        writer.println("{");
//        writer.println("\"error\": \"Unauthorized\",");
//        writer.println("\"message\": \"You must provide valid credentials to access this resource.\"");
//        writer.println("}");
        String json = String.format("{\"error\": \"%s\", \"message\": \"%s\"}",
                "Unauthorized",
                authException.getMessage());
        log.warn(json);
        writer.println(json);
    }}
