package com.sloth.api.lesson.service;

import com.sloth.api.lesson.dto.MainLessonDto;
import com.sloth.app.member.service.MemberService;
import com.sloth.domain.category.Category;
import com.sloth.domain.category.repository.CategoryRepository;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.repository.LessonRepository;
import com.sloth.domain.member.Member;
import com.sloth.domain.site.Site;
import com.sloth.domain.site.repository.SiteRepository;
import com.sloth.exception.BusinessException;
import com.sloth.exception.LessonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final MemberService memberService;
    private final SiteRepository siteRepository;
    private final CategoryRepository categoryRepository;

    public Lesson findLesson(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new LessonNotFoundException("해당하는 강의를 찾을 수 없습니다."));
    }

    public Lesson findLessonWithSiteCategory(Long id) {
        return lessonRepository.findLessonWithSiteCategoryByLessonId(id).orElseThrow(()->{
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

    public Long saveLesson(MainLessonDto.Request requestDto) {
        Member member = memberService.findMember(requestDto.getMemberId());

        Site site = siteRepository.findById(requestDto.getSiteId())
                .orElseThrow( () -> new BusinessException("사이트가 존재하지 않습니다."));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow( () -> new BusinessException("카테고리가 존재하지 않습니다."));

        Lesson lesson = Lesson.builder()
                .lessonName(requestDto.getName())
                .member(member)
                .alertDays(requestDto.getAlertDays())
                .totalNumber(requestDto.getTotalNumber())
                .price(requestDto.getPrice())
                .endDate(requestDto.getEndDate())
                .startDate(requestDto.getStartDate())
                .category(category)
                .site(site)
                .build();

        return lessonRepository.save(lesson).getLessonId();
    }
}