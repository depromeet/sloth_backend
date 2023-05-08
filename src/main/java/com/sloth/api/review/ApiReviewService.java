package com.sloth.api.review;

import com.sloth.domain.review.Review;
import com.sloth.domain.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiReviewService {

    private final ReviewService reviewService;

    @Transactional
    public ReviewSaveDto.Response saveReview(ReviewSaveDto.Request requestDto) {
        Review saveReview = requestDto.toEntity();
        saveReview = reviewService.saveReview(saveReview);
        return ReviewSaveDto.Response.of(saveReview);
    }

}
