package com.example.admin.unit.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.admin.controller.user.UserController;
import com.example.admin.handler.user.DeleteUserByIdHandler;
import com.example.admin.handler.user.GetAllUsersHandler;
import com.example.admin.handler.user.GetUserByIdHandler;
import com.example.admin.unit.AbstractUnitTest;
import com.example.persistence.model.role.RoleEnum;
import com.example.security.exception.detail.ForbiddenException;

public class UserControllerTest extends AbstractUnitTest {

    /* Mock with success responses from handler */
    @MockBean
    private GetAllUsersHandler getAllUsersHandler;

    @MockBean
    private GetUserByIdHandler getUserByIdHandler;

    @MockBean
    private DeleteUserByIdHandler deleteUserByIdHandler;

    @Test
    public void test_getAll_asAnonymous_forbidden() throws Exception {
        String routePath = UserController.COLLECTION_OF_USERS_URL;
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new ForbiddenException());

        getMockMvc().perform(MockMvcRequestBuilders.get(routePath))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }

    @Test
    public void test_getById_asAnonymous_forbidden() throws Exception {
        String routePath = getTestUtil().replaceRouteVariable(UserController.USER_ITEM_URL, "id", "1");
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new ForbiddenException());

        getMockMvc().perform(MockMvcRequestBuilders.get(routePath))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }

    @Test
    public void test_deleteById_asAnonymous_forbidden() throws Exception {
        String routePath = getTestUtil().replaceRouteVariable(UserController.USER_ITEM_URL, "id", "1");
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new ForbiddenException());

        getMockMvc().perform(MockMvcRequestBuilders.delete(routePath))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }

    @Test
    public void test_getAll_asUser_forbidden() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.USER);
        String routePath = UserController.COLLECTION_OF_USERS_URL;
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new ForbiddenException());

        getMockMvc().perform(MockMvcRequestBuilders.get(routePath).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }

    @Test
    public void test_getById_asUser_forbidden() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.USER);
        String routePath = getTestUtil().replaceRouteVariable(UserController.USER_ITEM_URL, "id", "1");
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new ForbiddenException());

        getMockMvc().perform(MockMvcRequestBuilders.get(routePath).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }

    @Test
    public void test_deleteById_asUser_forbidden() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.USER);
        String routePath = getTestUtil().replaceRouteVariable(UserController.USER_ITEM_URL, "id", "1");
        String expectedJson = getTestUtil().getAppExceptionBodyAsJsonString(new ForbiddenException());

        getMockMvc().perform(MockMvcRequestBuilders.delete(routePath).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson, true));
    }

    @Test
    public void test_getAll_asAdmin_ok() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.ADMIN);
        String routePath = UserController.COLLECTION_OF_USERS_URL;

        getMockMvc().perform(MockMvcRequestBuilders.get(routePath).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void test_getById_asAdmin_ok() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.ADMIN);
        String routePath = getTestUtil().replaceRouteVariable(UserController.USER_ITEM_URL, "id", "1");

        getMockMvc().perform(MockMvcRequestBuilders.get(routePath).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void test_deleteById_asAdmin_ok() throws Exception {
        HttpHeaders headers = getTestUtil().addAuthHeaders(getTestUtil().getHttpHeaders(), RoleEnum.ADMIN);
        String routePath = getTestUtil().replaceRouteVariable(UserController.USER_ITEM_URL, "id", "1");

        getMockMvc().perform(MockMvcRequestBuilders.delete(routePath).headers(headers))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}
