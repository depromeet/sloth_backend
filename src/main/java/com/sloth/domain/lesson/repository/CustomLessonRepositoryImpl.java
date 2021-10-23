package com.sloth.domain.lesson.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sloth.domain.lesson.Lesson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.sloth.domain.category.QCategory.category;
import static com.sloth.domain.lesson.QLesson.lesson;
import static com.sloth.domain.site.QSite.site;

@Repository
@RequiredArgsConstructor
public class CustomLessonRepositoryImpl implements CustomLessonRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Lesson> getDoingLessonsDetail(Long memberId) {
        return jpaQueryFactory
                .select(lesson)
                .from(lesson)
                .leftJoin(lesson.site, site).fetchJoin()
                .leftJoin(lesson.category, category).fetchJoin()
                .where(lesson.member.memberId.eq(memberId)
                                .and(lesson.isFinished.isFalse())
                                .and(lesson.startDate.loe(LocalDate.now())))
                .fetch();
    }

    @Override
    public List<Lesson> getLessonsDetail(Long memberId) {
        return jpaQueryFactory
                .selectFrom(lesson)
                .leftJoin(lesson.site, site).fetchJoin()
                .leftJoin(lesson.category, category).fetchJoin()
                .where(lesson.member.memberId.eq(memberId))
                .fetch();

    }
}
