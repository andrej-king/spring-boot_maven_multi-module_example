package com.example.main;

import lombok.Getter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.example.common.domain.util.AppUtil;
import com.example.common.domain.util.TestUtil;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.Random.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(AppUtil.PROFILE_TEST)
abstract public class AbstractTest {
    @Getter
    @Autowired
    private MockMvc mockMvc;

    @Getter
    @Autowired
    private TestUtil testUtil;
}
