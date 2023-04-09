package com.example.common.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.security.exception.detail.ForbiddenException;
import com.example.security.exception.detail.ResourceNotFoundException;
import com.example.security.exception.detail.SomethingWentWrongException;

@Log4j2
@RestController
public class AppErrorController extends BaseController implements ErrorController {

    private static final String ERROR_URL = "/error";

    /**
     * Route handler for error "NoHandlerFoundException"
     */
    @Operation(hidden = true)
    @RequestMapping(ERROR_URL)
    public void error(final HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (null != status && HttpStatus.NOT_FOUND.value() == Integer.parseInt(status.toString())) {
            throw new ResourceNotFoundException();
        }

        if (null != status && HttpStatus.FORBIDDEN.value() == Integer.parseInt(status.toString())) {
            throw new ForbiddenException();
        }

        throw new SomethingWentWrongException();
    }
}
