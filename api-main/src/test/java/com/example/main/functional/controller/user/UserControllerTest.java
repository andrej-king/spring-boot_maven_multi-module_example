package com.example.main.functional.controller.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.main.controller.user.UserController;
import com.example.main.dto.external.user.request.UpdatePasswordRequest;
import com.example.main.functional.AbstractFunctionalTest;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserControllerTest extends AbstractFunctionalTest {
    @BeforeEach
    public void init() {
        getTestUtil().resetAutoIncrementColumns(UserModel.TABLE_NAME);
        getTestUtil().insertTestAdmin();
        getTestUtil().insertTestUser();
    }

    @Test
    public void test_updatePassword_asUser_withTryLogin_success() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.USER);
        String updatePasswordRoutePath = UserController.USER_UPDATE_PASSWORD_URL;
        String newPassword = "someNewPassword";
        String requestContent = getTestUtil().asJsonString(
                new UpdatePasswordRequest(
                        getTestUtil().TEST_USER_PASSWORD,
                        newPassword
                )
        );

        assertNotEquals(getTestUtil().TEST_USER_PASSWORD, newPassword);
        /* update password */
        getMockMvc().perform(
                        MockMvcRequestBuilders
                                .put(updatePasswordRoutePath)
                                .headers(headers)
                                .content(requestContent)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}
