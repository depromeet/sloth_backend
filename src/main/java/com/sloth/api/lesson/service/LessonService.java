package com.sloth.api.lesson.service;

import com.sloth.api.lesson.dto.LessonUpdateDto;
import com.sloth.domain.category.Category;
import com.sloth.domain.category.repository.CategoryRepository;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.exception.LessonNotFoundException;
import com.sloth.domain.lesson.repository.LessonRepository;
import com.sloth.domain.member.Member;
import com.sloth.domain.site.Site;
import com.sloth.domain.site.repository.SiteRepository;
import com.sloth.global.exception.BusinessException;
import com.sloth.global.exception.ForbiddenException;
import com.sloth.global.exception.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final SiteRepository siteRepository;
    private final CategoryRepository categoryRepository;

    public Lesson findLessonWithSiteCategory(Member member, Long lessonId) {
        Lesson lesson = lessonRepository.findWithSiteCategoryMemberByLessonId(lessonId).orElseThrow(() -> {
            throw new LessonNotFoundException("해당하는 강의를 찾을 수 없습니다.");
        });
        checkAuthority(member, lesson);
        return lesson;
    }

    public Lesson updatePresentNumber(Member member, Long lessonId, int count) {
        Lesson lesson = findLessonWithMember(lessonId);
        checkAuthority(member, lesson);
        lesson.updatePresentNumber(count);
        return lesson;
    }

    private Lesson findLessonWithMember(Long lessonId) {
        return lessonRepository.findWithMemberByLessonId(lessonId).orElseThrow(() -> new LessonNotFoundException("해당하는 강의를 찾을 수 없습니다."));
    }

    public Long saveLesson(Lesson lesson, Long siteId, Long categoryId) {

        Site site = siteRepository.findById(siteId)
                .orElseThrow( () -> new BusinessException("사이트가 존재하지 않습니다."));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow( () -> new BusinessException("카테고리가 존재하지 않습니다."));

        lesson.updateSite(site);
        lesson.updateCategory(category);

        return lessonRepository.save(lesson).getLessonId();
    }

    public List<Lesson> getDoingLessons(Member member) {
        return lessonRepository.getDoingLessonsDetail(member.getMemberId());
    }

    public List<Lesson> getLessons(Member member) {
        return lessonRepository.getLessons(member.getMemberId());
    }

    /**
     * 강의 업데이트
     * @param member
     * @param request
     * @param lessonId
     * @return
     */
    public Lesson updateLesson(Member member, LessonUpdateDto.Request request, Long lessonId) {

        Lesson lesson = lessonRepository.findWithMemberByLessonId(lessonId)
                .orElseThrow(() -> new BusinessException("해당 강의가 존재하지 않습니다."));

        checkAuthority(member, lesson);

        Site site = siteRepository.findById(request.getSiteId())
                .orElseThrow( () -> new BusinessException("사이트가 존재하지 않습니다."));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow( () -> new BusinessException("카테고리가 존재하지 않습니다."));

        if (request.getPrice() < 0) {
            throw new InvalidParameterException("강의 금액은 0원보다 적을 수 없습니다.");
        }

        lesson.updateLesson(request.getLessonName(), request.getTotalNumber(),request.getPrice(), category, site);

        return lesson;
    }


    private void checkAuthority(Member member, Lesson lesson) {
        if (!StringUtils.equals(lesson.getMember().getMemberId(), member.getMemberId())) {
            throw new ForbiddenException("해당 강의에 대한 권한이 없습니다.");
        }
    }

    public void deleteLesson(Member member, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("해당하는 강의를 찾을 수 없습니다."));

        if(lesson.getMember().getMemberId() != member.getMemberId()) {
            throw new BusinessException("회원 아이디와 강의 소유주가 다릅니다.");
        }

        lessonRepository.delete(lesson);
    }

    public void sortByRemainDay(List<Lesson> lessons) {
        lessons.sort(new Comparator<Lesson>() {
            @Override
            public int compare(Lesson standardLesson, Lesson compareLesson) {
                int standardLessonRemainDay = standardLesson.getRemainDay(LocalDate.now());
                int compareLessonRemainDay = compareLesson.getRemainDay(LocalDate.now());
                return Integer.compare(standardLessonRemainDay, compareLessonRemainDay);
            }
        });
    }
}