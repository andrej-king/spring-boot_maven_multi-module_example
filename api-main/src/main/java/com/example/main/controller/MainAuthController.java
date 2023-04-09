package com.example.main.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import com.example.common.application.controller.BaseController;

@SecurityRequirement(name = "api")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority(T(com.example.common.application.controller.BaseController).AUTHORITY_USER)")
abstract public class MainAuthController extends BaseController {
}
