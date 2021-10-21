package com.sloth.domain.lesson.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sloth.domain.category.QCategory;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.QLesson;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.sloth.domain.category.QCategory.category;
import static com.sloth.domain.lesson.QLesson.lesson;
import static com.sloth.domain.site.QSite.site;

public class CustomLessonRepositoryImpl implements CustomLessonRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public CustomLessonRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

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
}
