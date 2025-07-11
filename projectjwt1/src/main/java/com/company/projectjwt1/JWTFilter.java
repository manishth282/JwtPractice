package com.company.projectjwt1;

import com.company.projectjwt1.service.CustomUserDetailsService;
import com.company.projectjwt1.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    @Autowired
    private JWTService jwtService;

    @Autowired  /* Its bean is get build automatically when application starts*/
    private ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.warn("************** JWTFilter method name doFilterInternal......");
        String authHeaderStr = request.getHeader("Authorization");
//        System.out.println("JWTFilter ::::: \n AuthHeaderString ::::: " + authHeaderStr);

        String token = null;
        String username = null;

        try {
            if (authHeaderStr != null && authHeaderStr.startsWith("Bearer ")) {
                token = authHeaderStr.substring(7);
//                System.out.println("Token ::: " + token);
                username = jwtService.extractUsername(token);
//                System.out.println("Extracted Username ::::::: " + username);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = applicationContext.getBean(CustomUserDetailsService.class)
                        .loadUserByUsername(username);
//                System.out.println("UserDetails ::::: " + userDetails);

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                    System.out.println("SecurityContext is set with authentication ::::: ");
                }
            }

            filterChain.doFilter(request, response);

        } catch (RuntimeException ex) {
            // Log and send a clean error response
            logger.warn("JWT validation failed: {}");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid JWT: " + ex.getMessage() + "\"}");
            response.getWriter().flush();
        }
    }
}
