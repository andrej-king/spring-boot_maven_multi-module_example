package com.example.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Log4j2
public class JwtTokenAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtTokenService;

    public static final String TOKEN_PREFIX = "bearer ";

    /**
     * JWT authentication
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // pass request down the chain, except for OPTIONS requests
        if (!RequestMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            Optional.ofNullable((request).getHeader(HttpHeaders.AUTHORIZATION))
                    .filter(authHeader -> authHeader.toLowerCase().startsWith(TOKEN_PREFIX))
                    .map(authHeader -> authHeader.substring(TOKEN_PREFIX.length()))
                    .filter(jwtTokenService::validateToken)
                    .map(this::getUsernamePasswordAuthentication)
                    .ifPresent(authenticationToken -> {
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    });
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Set authentication username and authorities
     */
    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthentication(String token) {
        log.debug(String.format(
                "getUsernamePasswordAuthentication.extractAuthorities = %s", jwtTokenService.extractAuthorities(token)
        ));

        return new UsernamePasswordAuthenticationToken(
                jwtTokenService.extractUsername(token),
                null,
                jwtTokenService.extractAuthorities(token)
        );
    }
}
