package com.example.admin.functional.controller.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.admin.controller.user.UserController;
import com.example.admin.functional.AbstractFunctionalTest;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;
import com.example.security.exception.detail.DeleteAdminException;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserControllerTest extends AbstractFunctionalTest {

    @BeforeEach
    public void init() {
        getTestUtil().resetAutoIncrementColumns(UserModel.TABLE_NAME);
        getTestUtil().insertTestAdmin();
        getTestUtil().insertTestUser();
    }

    @Test
    public void test_getAll_asAdmin_ok() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.ADMIN);
        String routePath = UserController.COLLECTION_OF_USERS_URL;

        getMockMvc().perform(MockMvcRequestBuilders.get(routePath).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))

                // admin JSON object
                .andExpect(jsonPath("[1].id").value(getTestUtil().getAdminId()))
                .andExpect(jsonPath("[1].uuid").isString())
                .andExpect(jsonPath("[1].uuid").isNotEmpty())
                .andExpect(jsonPath("[1].login").value(getTestUtil().TEST_ADMIN_LOGIN))
                .andExpect(jsonPath("[1].email").value(getTestUtil().TEST_ADMIN_EMAIL))
                .andExpect(jsonPath("[1].password").doesNotExist())
                .andExpect(jsonPath("[1].fullName").value(getTestUtil().TEST_ADMIN_FULL_NAME))
                .andExpect(jsonPath("[1].enabled").isBoolean())
                .andExpect(jsonPath("[1].enabled").value(true))
                .andExpect(jsonPath("[1].role.id").value(getTestUtil().getAdminRoleId()))
                .andExpect(jsonPath("[1].role.uuid").isString())
                .andExpect(jsonPath("[1].role.uuid").isNotEmpty())
                .andExpect(jsonPath("[1].role.name").value(RoleEnum.ADMIN.name()))

                // user JSON object
                .andExpect(jsonPath("[0].id").value(getTestUtil().getUserId()))
                .andExpect(jsonPath("[0].uuid").isString())
                .andExpect(jsonPath("[0].uuid").isNotEmpty())
                .andExpect(jsonPath("[0].login").value(getTestUtil().TEST_USER_LOGIN))
                .andExpect(jsonPath("[0].email").value(getTestUtil().TEST_USER_EMAIL))
                .andExpect(jsonPath("[0].password").doesNotExist())
                .andExpect(jsonPath("[0].fullName").value(getTestUtil().TEST_USER_FULL_NAME))
                .andExpect(jsonPath("[0].enabled").isBoolean())
                .andExpect(jsonPath("[0].enabled").value(true))
                .andExpect(jsonPath("[0].role.id").value(getTestUtil().getUserRoleId()))
                .andExpect(jsonPath("[0].role.uuid").isString())
                .andExpect(jsonPath("[0].role.uuid").isNotEmpty())
                .andExpect(jsonPath("[0].role.name").value(RoleEnum.USER.name()));
    }

    @Test
    public void test_getAdminById_asAdmin_ok() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.ADMIN);
        String routePath = getTestUtil().replaceRouteVariable(
                UserController.USER_ITEM_URL, "id", getTestUtil().getAdminId().toString()
        );

        getMockMvc().perform(MockMvcRequestBuilders.get(routePath).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(getTestUtil().getAdminId()))
                .andExpect(jsonPath("$.uuid").isString())
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.login").value(getTestUtil().TEST_ADMIN_LOGIN))
                .andExpect(jsonPath("$.email").value(getTestUtil().TEST_ADMIN_EMAIL))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.fullName").value(getTestUtil().TEST_ADMIN_FULL_NAME))
                .andExpect(jsonPath("$.enabled").isBoolean())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.role.id").value(getTestUtil().getAdminRoleId()))
                .andExpect(jsonPath("$.role.uuid").isString())
                .andExpect(jsonPath("$.role.uuid").isNotEmpty())
                .andExpect(jsonPath("$.role.name").value(RoleEnum.ADMIN.name()));
    }

    @Test
    public void test_getUserById_asAdmin_ok() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.ADMIN);
        String routePath = getTestUtil().replaceRouteVariable(
                UserController.USER_ITEM_URL, "id", getTestUtil().getUserId().toString()
        );

        getMockMvc().perform(MockMvcRequestBuilders.get(routePath).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(getTestUtil().getUserId()))
                .andExpect(jsonPath("$.uuid").isString())
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.login").value(getTestUtil().TEST_USER_LOGIN))
                .andExpect(jsonPath("$.email").value(getTestUtil().TEST_USER_EMAIL))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.fullName").value(getTestUtil().TEST_USER_FULL_NAME))
                .andExpect(jsonPath("$.enabled").isBoolean())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.role.id").value(getTestUtil().getUserRoleId()))
                .andExpect(jsonPath("$.role.uuid").isString())
                .andExpect(jsonPath("$.role.uuid").isNotEmpty())
                .andExpect(jsonPath("$.role.name").value(RoleEnum.USER.name()));
    }

    @Test
    public void test_deleteUser_asAdmin_ok() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.ADMIN);
        String routePath = getTestUtil().replaceRouteVariable(
                UserController.USER_ITEM_URL, "id", getTestUtil().getUserId().toString()
        );

        /* check exists user and admin */
        getMockMvc().perform(MockMvcRequestBuilders.get(UserController.COLLECTION_OF_USERS_URL).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("[0].id").value(getTestUtil().getUserId()))
                .andExpect(jsonPath("[1].id").value(getTestUtil().getAdminId()));

        /* delete user */
        getMockMvc().perform(MockMvcRequestBuilders.delete(routePath).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        /* check exists only admin */
        getMockMvc().perform(MockMvcRequestBuilders.get(UserController.COLLECTION_OF_USERS_URL).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("[0].id").value(getTestUtil().getAdminId()));
    }

    @Test
    public void test_deleteAdmin_asAdmin_fail() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.ADMIN);
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new DeleteAdminException());
        String routePath = getTestUtil().replaceRouteVariable(
                UserController.USER_ITEM_URL, "id", getTestUtil().getAdminId().toString()
        );

        /* check exists user and admin */
        getMockMvc().perform(MockMvcRequestBuilders.get(UserController.COLLECTION_OF_USERS_URL).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("[0].id").value(getTestUtil().getUserId()))
                .andExpect(jsonPath("[1].id").value(getTestUtil().getAdminId()));

        /* try to delete admin */
        getMockMvc().perform(MockMvcRequestBuilders.delete(routePath).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));

        /* check exists user and admin */
        getMockMvc().perform(MockMvcRequestBuilders.get(UserController.COLLECTION_OF_USERS_URL).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("[0].id").value(getTestUtil().getUserId()))
                .andExpect(jsonPath("[1].id").value(getTestUtil().getAdminId()));
    }
}
