package com.sloth.domain.memberToken.repository;

import com.sloth.domain.memberToken.MemberToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {



}
