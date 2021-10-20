package com.sloth.domain.lesson.repository;

import com.sloth.domain.lesson.Lesson;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @EntityGraph(attributePaths = {"site","category"})
    Optional<Lesson> findLessonWithSiteCategoryByLessonId(Long id);

    Optional<Lesson> findByLessonName(String name);
}
