package com.sloth.domain.nickname.repository;

import com.sloth.domain.nickname.Nickname;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class NicknameRepositoryTest {

    @Autowired
    private NicknameRepository nicknameRepository;

    @Test
    void findRandomNickname() {

        // when
        Nickname randomNickname = nicknameRepository.findRandomNickname();

        // then
        Assertions.assertThat(randomNickname).isNotNull();
        Assertions.assertThat(randomNickname.getName()).isNotNull();
        Assertions.assertThat(randomNickname.getNicknameId()).isNotNull();
    }

}