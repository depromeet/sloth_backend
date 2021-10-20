package com.sloth.domain.member.repository;

import com.sloth.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Page<Member> findByMemberName(String name, Pageable pageable);

    boolean existsByEmail(String email);

    @EntityGraph("Member.withAll")
    //@Query("select m from Member m join fetch m.lessons l join fetch l.category c join fetch l.site s")
    Optional<Member> findWithAllByMemberId(Long id);
}