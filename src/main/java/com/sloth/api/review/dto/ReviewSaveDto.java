package com.sloth.api.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sloth.domain.member.Member;
import com.sloth.domain.review.Review;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class ReviewSaveDto {

    @ApiModel(value = "리뷰 저장 요청 객체")
    @Getter @Setter
    public static class Request {

        @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
        @NotNull(message = "리뷰 날짜를 입력해주세요")
        @ApiModelProperty(value = "리뷰 날짜", required = true)
        private LocalDate reviewDate;

        @NotBlank(message = "리뷰 내용을 입력해주세요")
        @Size(max = 1000, message = "리뷰 내용은 최대 1000자까지 입력 가능합니다.")
        @ApiModelProperty(value = "리뷰 내용(최대1000자)", required = true)
        private String reviewContent;

        public Review toEntity(Member member) {
            return Review.builder()
                    .reviewDate(reviewDate)
                    .reviewContent(reviewContent)
                    .member(member)
                    .build();
        }
    }

    @ApiModel(value = "리뷰 저장 반환 객체")
    @AllArgsConstructor @NoArgsConstructor
    @Getter @Builder
    public static class Response {

        @ApiModelProperty(value = "리뷰 아이디", required = true)
        private Long reviewId;

        @ApiModelProperty(value = "리뷰 날짜", required = true)
        private LocalDate reviewDate;

        @ApiModelProperty(value = "리뷰 내용(최대1000자)", required = true)
        private String reviewContent;

        public static ReviewSaveDto.Response of(Review review) {
            return Response.builder()
                    .reviewId(review.getReviewId())
                    .reviewDate(review.getReviewDate())
                    .reviewContent(review.getReviewContent())
                    .build();
        }

    }

}