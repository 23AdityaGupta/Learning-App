package com.learningapp.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Header check: "Bearer <token>" format hona chahiye
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7); // "Bearer " ke baad ka part

            if (jwtUtil.validateToken(token)) {

                String username = jwtUtil.extractUsername(token);

                // Spring Security ko batao — "ye user authenticated hai"
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username, null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Request ko aage jaane do
        filterChain.doFilter(request, response);
    }
}