package com.sloth.domain.memberToken.repository;

import com.sloth.domain.memberToken.MemberToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {

    Optional<MemberToken> findByRefreshToken(String refreshToken);

    @Query("SELECT mt " +
            "FROM MemberToken mt " +
            "WHERE mt.member.memberId = :memberId")
    MemberToken findByMemberId(@Param("memberId") Long memberId);

}
