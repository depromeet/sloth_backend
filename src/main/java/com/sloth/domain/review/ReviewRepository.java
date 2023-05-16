package com.sloth.domain.review;

import com.sloth.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByMemberAndReviewId(Member member, Long reviewId);

    Optional<Review> findByMemberAndReviewDate(Member member, LocalDate reviewDate);
}