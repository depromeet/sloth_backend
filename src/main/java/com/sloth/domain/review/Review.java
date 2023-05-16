package com.sloth.domain.review;

import com.sloth.domain.common.BaseEntity;
import com.sloth.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "reviewId", callSuper = false)
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private LocalDate reviewDate;

    @Column(nullable = false, length = 1000)
    private String reviewContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
