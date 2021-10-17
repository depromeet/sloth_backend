package com.sloth.api.lesson.service;

import com.sloth.api.lesson.dto.RequestLessonDto;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ApiLessionService {

    private final LessonRepository lessonRepository;

    @Transactional
    public Long save(RequestLessonDto requestDto) {
        return lessonRepository.save(requestDto.toEntity()).getId();
    }
}