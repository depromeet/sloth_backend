package com.sloth.domain.review;

import com.sloth.domain.member.Member;
import com.sloth.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomReviewRepository customReviewRepository;

    @Transactional
    public Review saveReview(Review review) {
        Optional<Review> optionalReview = reviewRepository.findByMemberAndReviewDate(review.getMember(), review.getReviewDate());
        if(optionalReview.isPresent()) {
           throw new BusinessException("해당 날짜에 리뷰가 존재합니다.");
        }
        return reviewRepository.save(review);
    }

    public List<Review> findReviewsByMemberAndSearchDate(Member member, LocalDate searchStartDate,
                                                LocalDate searchEndDate) {
        return customReviewRepository.findReviewsByMemberAndSearchDate(member.getMemberId(), searchStartDate, searchEndDate);
    }

    public Review findByMemberAndReviewId(Member member, Long reviewId) {
        return reviewRepository.findByMemberAndReviewId(member, reviewId)
                .orElseThrow(() -> {throw new BusinessException("해당 리뷰가 존재하지 않습니다.");});
    }

}
