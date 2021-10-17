package com.sloth.domain.lesson.service;

import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.repository.LessonRepository;
import com.sloth.exception.LessonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    public Lesson findLesson(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new LessonNotFoundException("해당하는 강의를 찾을 수 없습니다."));
    }

    public Lesson findLessonWithSiteCategory(Long id) {
         return lessonRepository.findById(id).orElseThrow(()->{
             throw new LessonNotFoundException("해당하는 강의를 찾을 수 없습니다.");
         });
    }

}
