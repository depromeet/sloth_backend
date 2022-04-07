package com.sloth.test.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.global.config.auth.TokenProvider;
import com.sloth.test.config.TestApiConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public class NewBaseApiController {

    protected MockMvc mockMvc;

    protected String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJhdWQiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MzcxNjM3NjUsImV4cCI6MTE2MzcxNjM3NjV9.9fwwJ6FC_36WwZi2AyAV1VY6SkVdyO6G7Mmr6B9MtSvy4SIwPyWl3G8qUjoZzy4g7gSpRqV-0kQBdB8t2Mm2Tw";

    protected final String testEmail = "email@email.com";

    protected ObjectMapper objectMapper = new ObjectMapper();
}
