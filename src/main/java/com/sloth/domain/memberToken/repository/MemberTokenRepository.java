package com.sloth.domain.memberToken.repository;

import com.sloth.domain.memberToken.MemberToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {

    Optional<MemberToken> findByRefreshToken(String refreshToken);

}
