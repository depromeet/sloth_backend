package com.sloth.domain.nickname.service;

import com.sloth.domain.nickname.Nickname;
import com.sloth.creator.TestNicknameCreator;
import com.sloth.domain.nickname.repository.NicknameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class) // 테스트 클래스가 Mockito를 사용함을 의미
public class NicknameServiceTest {

    @Mock
    NicknameRepository nicknameRepository;

    private NicknameService nicknameService;
    private TestNicknameCreator testNicknameCreator;

    @BeforeEach
    public void init() {
        nicknameService = new NicknameService(nicknameRepository);
        testNicknameCreator = new TestNicknameCreator();
    }


    @Test
    @DisplayName("랜덤 닉네임 조회 테스트")
    public void findRandomNickname() {
        //given
        Nickname nickname = testNicknameCreator.createNickname();

        given(nicknameService.findRandomNickname())
                .willReturn(nickname);

        // when
        Nickname randomNickname = nicknameService.findRandomNickname();

        // then
        assertThat(randomNickname).isNotNull();
        assertThat(randomNickname.getName()).isEqualTo(nickname.getName());
    }

}