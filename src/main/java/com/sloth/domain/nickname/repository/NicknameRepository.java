package com.sloth.domain.nickname.repository;

import com.sloth.domain.nickname.Nickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NicknameRepository extends JpaRepository<Nickname, Long> {

    @Query(value = "SELECT nickname_id " +
                   ", name " +
                   ", is_used " +
            "FROM nickname " +
            "where is_used = false " +
            "ORDER BY rand() " +
            "LIMIT 1", nativeQuery = true)
    Nickname findRandomNickname();

}
