package com.sloth.api.review;

import com.sloth.api.review.dto.ReviewDetailResponseDto;
import com.sloth.api.review.dto.ReviewRequestDto;
import com.sloth.api.review.dto.ReviewSaveDto;
import com.sloth.api.review.dto.ReviewUpdateDto;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.domain.review.Review;
import com.sloth.domain.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiReviewService {

    private final MemberService memberService;
    private final ReviewService reviewService;

    @Transactional
    public ReviewSaveDto.Response saveReview(ReviewSaveDto.Request requestDto, String email) {
        Member member = memberService.findByEmail(email);
        Review saveReview = requestDto.toEntity(member);
        saveReview = reviewService.saveReview(saveReview);
        return ReviewSaveDto.Response.of(saveReview);
    }

    public List<ReviewRequestDto.Response> findReviews(ReviewRequestDto.Request requestDto, String email) {
        Member member = memberService.findByEmail(email);
        List<Review> revies = reviewService.findReviewsByMemberAndSearchDate(member, requestDto.getSearchStartDate(), requestDto.getSearchEndDate());
        return revies.stream().map(it -> ReviewRequestDto.Response.of(it))
                .collect(Collectors.toList());
    }

    public ReviewDetailResponseDto findReview(Long reviewId, String email) {
        Member member = memberService.findByEmail(email);
        Review review = reviewService.findByMemberAndReviewId(member, reviewId);
        return ReviewDetailResponseDto.of(review);
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateDto.Request requestDto, String email) {
        Member member = memberService.findByEmail(email);
        Review savedReview = reviewService.findByMemberAndReviewId(member, reviewId);
        savedReview.updateReviewContent(requestDto.getReviewContent());
    }

}
