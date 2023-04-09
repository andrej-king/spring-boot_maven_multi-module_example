package com.example.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.example.security.exception.ErrorMessages;
import com.example.security.exception.detail.UnauthorizedException;

import java.io.IOException;

public class AuthEntryPointImpl implements AuthenticationEntryPoint {
    /**
     * {@inheritDoc}
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        ErrorMessages errorMessages = new ErrorMessages();
        errorMessages.append(UnauthorizedException.MSG);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(errorMessages);

        /* print error as JSON response */
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(UnauthorizedException.STATUS.value());
        response.getOutputStream().println(json);
    }
}
