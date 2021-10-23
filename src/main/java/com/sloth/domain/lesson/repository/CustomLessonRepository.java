package com.sloth.domain.lesson.repository;

import com.sloth.domain.lesson.Lesson;

import java.util.List;

public interface CustomLessonRepository {
    List<Lesson> getDoingLessonsDetail(Long memberId);

    List<Lesson> getLessons(Long memberId);
}
