package com.example.fitnessrecord.domain.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.fitnessrecord.domain.user.dto.RegisterUserInput;
import com.google.gson.Gson;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
@ActiveProfiles("dev")
class UserControllerTest {

  MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void mockMvcSetUp(){
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .addFilter(new CharacterEncodingFilter("UTF-8", true))
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .build();
  }

  @Test
  void register() throws Exception {
    String email = "test@test.com";
    String password = "test";
    String nickname = "test_nickname";

    RegisterUserInput input = RegisterUserInput.builder()
        .email(email)
        .password(password)
        .passwordCheck(password)
        .nickname(nickname)
        .build();
    Gson gson = new Gson();
    String req = gson.toJson(input);

    mockMvc.perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(email))
        .andExpect(jsonPath("$.nickname").value(nickname))
        .andExpect(jsonPath("$.createdAt").exists())
        .andExpect(jsonPath("$.userType").exists());
  }

  @Test
  void emailAuth() {
  }

}