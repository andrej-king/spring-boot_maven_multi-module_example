package com.example.admin.functional.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.admin.controller.auth.AuthController;
import com.example.admin.functional.AbstractFunctionalTest;
import com.example.common.application.dto.external.auth.request.RefreshTokenRequest;
import com.example.common.application.dto.external.auth.request.SignInRequest;
import com.example.common.application.dto.external.auth.response.SignInResponse;
import com.example.persistence.model.user.UserModel;
import com.example.security.exception.detail.LoginInfoException;
import com.example.security.exception.detail.RefreshTokenException;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AuthControllerTest extends AbstractFunctionalTest {
    @BeforeEach
    public void init() {
        getTestUtil().resetAutoIncrementColumns(UserModel.TABLE_NAME);
        getTestUtil().insertTestAdmin();
    }

    @Test
    public void test_signIn_wrongLogin() throws Exception {
        HttpHeaders headers = getTestUtil().getHttpHeaders();
        String routePath = AuthController.SIGN_IN_URL;
        SignInRequest request = new SignInRequest(
                getTestUtil().TEST_USER_LOGIN,
                getTestUtil().TEST_ADMIN_PASSWORD
        );
        String requestContent = getTestUtil().asJsonString(request);
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new LoginInfoException());

        getMockMvc().perform(MockMvcRequestBuilders
                        .post(routePath)
                        .headers(headers)
                        .content(requestContent)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }

    @Test
    public void test_signIn_wrongPassword() throws Exception {
        HttpHeaders headers = getTestUtil().getHttpHeaders();
        String routePath = AuthController.SIGN_IN_URL;
        SignInRequest request = new SignInRequest(
                getTestUtil().TEST_ADMIN_LOGIN,
                getTestUtil().TEST_USER_PASSWORD
        );
        String requestContent = getTestUtil().asJsonString(request);
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new LoginInfoException());

        getMockMvc().perform(MockMvcRequestBuilders
                        .post(routePath)
                        .headers(headers)
                        .content(requestContent)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }

    @Test
    public void test_signIn_success() throws Exception {
        HttpHeaders headers = getTestUtil().getHttpHeaders();
        String routePath = AuthController.SIGN_IN_URL;
        SignInRequest request = new SignInRequest(
                getTestUtil().TEST_ADMIN_LOGIN,
                getTestUtil().TEST_ADMIN_PASSWORD
        );
        String requestContent = getTestUtil().asJsonString(request);

        getMockMvc().perform(MockMvcRequestBuilders
                        .post(routePath)
                        .headers(headers)
                        .content(requestContent)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    public void test_refreshToken_success() throws Exception {
        HttpHeaders headers = getTestUtil().getHttpHeaders();
        String signInRoutePath = AuthController.SIGN_IN_URL;
        SignInRequest signInRequest = new SignInRequest(
                getTestUtil().TEST_ADMIN_LOGIN,
                getTestUtil().TEST_ADMIN_PASSWORD
        );
        String signInRequestContent = getTestUtil().asJsonString(signInRequest);

        /* auth for get refresh token */
        String authJsonResult = getMockMvc().perform(MockMvcRequestBuilders
                        .post(signInRoutePath)
                        .headers(headers)
                        .content(signInRequestContent)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        SignInResponse signInResponse = new ObjectMapper().readValue(authJsonResult, SignInResponse.class);

        /* send refresh token for get new JWT auth token */
        String refreshRoutePath = AuthController.REFRESH_TOKEN_URL;
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest(signInResponse.refreshToken());
        String refreshRequestContent = getTestUtil().asJsonString(refreshRequest);

        getMockMvc().perform(MockMvcRequestBuilders
                        .post(refreshRoutePath)
                        .headers(headers)
                        .content(refreshRequestContent)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    public void test_refreshToken_invalidFormat() throws Exception {
        HttpHeaders headers = getTestUtil().getHttpHeaders();
        String refreshRoutePath = AuthController.REFRESH_TOKEN_URL;
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest("abc");
        String refreshRequestContent = getTestUtil().asJsonString(refreshRequest);
        String expectedJson = "{\"errors\":{\"body\":[\"[refreshToken] must be a valid UUID. your input: [abc]\"]}}";

        getMockMvc().perform(MockMvcRequestBuilders
                        .post(refreshRoutePath)
                        .headers(headers)
                        .content(refreshRequestContent)
                )
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }

    @Test
    public void test_refreshToken_notFound() throws Exception {
        HttpHeaders headers = getTestUtil().getHttpHeaders();
        String refreshRoutePath = AuthController.REFRESH_TOKEN_URL;
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest(UUID.randomUUID().toString());
        String refreshRequestContent = getTestUtil().asJsonString(refreshRequest);
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new RefreshTokenException());

        getMockMvc().perform(MockMvcRequestBuilders
                        .post(refreshRoutePath)
                        .headers(headers)
                        .content(refreshRequestContent)
                )
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }
}
