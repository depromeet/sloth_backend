package com.sloth.domain.lesson.repository;

import com.sloth.domain.lesson.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface LessonRepository extends JpaRepository<Lesson, Long> {

}
