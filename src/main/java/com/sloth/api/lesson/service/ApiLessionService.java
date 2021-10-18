package com.sloth.api.lesson.service;

import com.sloth.api.lesson.dto.RequestLessonDto;
import com.sloth.app.member.service.MemberService;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.repository.LessonRepository;
import com.sloth.exception.LessonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ApiLessionService {

    private final LessonRepository lessonRepository;

    private final MemberService memberService;

    public Lesson findLesson(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new LessonNotFoundException("해당하는 강의를 찾을 수 없습니다."));
    }

    public Lesson findLessonWithSiteCategory(Long id) {
        return lessonRepository.findLessonWithSiteCategoryById(id).orElseThrow(()->{
            throw new LessonNotFoundException("해당하는 강의를 찾을 수 없습니다.");
        });
    }

    public List<Lesson> getDoingLessons(Long memberId) {
        List<Lesson> lessons = memberService.findMemberWithAll(memberId).getLessons();
        return lessons.stream().filter(Lesson::isDoingLesson).collect(Collectors.toList()); // TODO 로직 개선 필요할 듯
    }

    public Lesson plusPresentNumber(Long id, int count) {
        Lesson lesson = findLesson(id);
        lesson.plusPresentNumber(count);
        return lesson;
    }

    public Lesson minusPresentNumber(Long id, int count) {
        Lesson lesson = findLesson(id);
        lesson.minusPresentNumber(count);
        return lesson;
    }

    @Transactional
    public Long save(RequestLessonDto requestDto) {
        return lessonRepository.save(requestDto.toEntity()).getId();
    }
}