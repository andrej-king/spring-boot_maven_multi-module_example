package com.example.main.unit.controller.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.common.application.dto.external.auth.request.RefreshTokenRequest;
import com.example.common.application.dto.external.auth.request.SignInRequest;
import com.example.common.application.dto.external.auth.request.SignUpRequest;
import com.example.common.application.handler.auth.refresh.RefreshTokenHandler;
import com.example.common.application.handler.auth.signin.SignInHandler;
import com.example.common.application.handler.auth.signup.SignUpHandler;
import com.example.main.controller.auth.AuthController;
import com.example.main.unit.AbstractUnitTest;

import java.util.UUID;

public class AuthControllerTest extends AbstractUnitTest {
    @MockBean
    private SignInHandler signInHandler;

    @MockBean
    private SignUpHandler signUpHandler;

    @MockBean
    private RefreshTokenHandler refreshTokenHandler;

    @Test
    public void test_signIn_success() throws Exception {
        String routePath = AuthController.SIGN_IN_URL;
        HttpHeaders headers = getTestUtil().getHttpHeaders();
        SignInRequest request = new SignInRequest("testlogin", "someNewPassword");
        String requestContent = getTestUtil().asJsonString(request);

        getMockMvc().perform(MockMvcRequestBuilders
                        .post(routePath)
                        .headers(headers)
                        .content(requestContent)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void test_signUp_success() throws Exception {
        String routePath = AuthController.SIGN_UP_URL;
        HttpHeaders headers = getTestUtil().getHttpHeaders();
        String login = "john2";
        String email = "test123@example.com";
        String password = "somePassword";
        String fullName = "John Doe";

        SignUpRequest request = new SignUpRequest(login, email, password, fullName);
        String requestContent = getTestUtil().asJsonString(request);

        getMockMvc().perform(MockMvcRequestBuilders
                        .post(routePath)
                        .headers(headers)
                        .content(requestContent)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void test_refreshToken_success() throws Exception {
        String routePath = AuthController.REFRESH_TOKEN_URL;
        HttpHeaders headers = getTestUtil().getHttpHeaders();
        RefreshTokenRequest request = new RefreshTokenRequest(UUID.randomUUID().toString());
        String requestContent = getTestUtil().asJsonString(request);

        getMockMvc().perform(MockMvcRequestBuilders
                        .post(routePath)
                        .headers(headers)
                        .content(requestContent)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}
