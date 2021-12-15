package com.sloth.domain.nickname.service;

import com.sloth.domain.nickname.Nickname;
import com.sloth.domain.nickname.repository.NicknameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NicknameService {

    private final NicknameRepository nicknameRepository;

    public Nickname findRandomNickname() {
        return nicknameRepository.findRandomNickname();
    }

}
