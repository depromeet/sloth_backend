package com.sloth.domain.lesson.repository;

import com.sloth.domain.lesson.Lesson;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface CustomLessonRepository {
    List<Lesson> getDoingLessonsDetail(Long memberId);

    List<Lesson> getLessons(Long memberId);
}
