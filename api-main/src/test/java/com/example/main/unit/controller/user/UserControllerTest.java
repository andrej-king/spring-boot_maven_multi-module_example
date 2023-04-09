package com.example.main.unit.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.main.controller.user.UserController;
import com.example.main.dto.external.user.request.UpdatePasswordRequest;
import com.example.main.handler.user.UpdatePasswordHandler;
import com.example.main.unit.AbstractUnitTest;
import com.example.persistence.model.role.RoleEnum;
import com.example.security.exception.detail.ForbiddenException;

public class UserControllerTest extends AbstractUnitTest {
    /* Mock with success responses from handler */
    @MockBean
    private UpdatePasswordHandler updatePasswordHandler;

    @Test
    public void test_updatePassword_asAnonymous_forbidden() throws Exception {
        String routePath = UserController.USER_UPDATE_PASSWORD_URL;
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new ForbiddenException());

        getMockMvc().perform(MockMvcRequestBuilders.put(routePath)
                        .content(getValidRequestJson())
                        .headers(getTestUtil().getHttpHeaders())
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }

    @Test
    public void test_updatePassword_asAdmin_forbidden() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.ADMIN);
        String routePath = UserController.USER_UPDATE_PASSWORD_URL;
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new ForbiddenException());

        getMockMvc().perform(MockMvcRequestBuilders.put(routePath)
                        .content(getValidRequestJson())
                        .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }

    @Test
    public void test_updatePassword_asUser_success() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.USER);
        String routePath = UserController.USER_UPDATE_PASSWORD_URL;

        getMockMvc().perform(MockMvcRequestBuilders.put(routePath)
                        .content(getValidRequestJson())
                        .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Get UpdatePasswordRequest object as json
     */
    private String getValidRequestJson() {
        UpdatePasswordRequest request = new UpdatePasswordRequest("someOldPassword", "someNewPassword");

        return getTestUtil().asJsonString(request);
    }
}
