package com.sloth.domain.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.repository.CustomLessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.sloth.domain.category.QCategory.category;
import static com.sloth.domain.lesson.QLesson.lesson;
import static com.sloth.domain.review.QReview.*;
import static com.sloth.domain.site.QSite.site;

@Repository
@RequiredArgsConstructor
public class CustomReviewRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Review> findReviewsByMemberAndSearchDate(Long memberId,
                                                         LocalDate searchStartDate, LocalDate searchEndDate) {
        return jpaQueryFactory
                .selectFrom(review)
                .where(
                        review.member.memberId.eq(memberId),
                        review.reviewDate.between(searchStartDate, searchEndDate)
                      )
                .orderBy(review.reviewDate.asc())
                .fetch();
    }

}
