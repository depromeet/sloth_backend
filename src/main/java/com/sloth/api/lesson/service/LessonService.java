package com.sloth.api.lesson.service;

import com.sloth.api.lesson.dto.LessonCreateDto;
import com.sloth.api.lesson.dto.LessonUpdateDto;
import com.sloth.api.lesson.dto.RenderOrderDto;
import com.sloth.domain.category.Category;
import com.sloth.domain.category.repository.CategoryRepository;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.repository.LessonRepository;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.domain.site.Site;
import com.sloth.domain.site.repository.SiteRepository;
import com.sloth.exception.BusinessException;
import com.sloth.exception.ForbiddenException;
import com.sloth.exception.LessonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final SiteRepository siteRepository;
    private final CategoryRepository categoryRepository;

    public Lesson findLessonWithSiteCategory(Member member, Long id) {
        Lesson lesson = lessonRepository.findWithSiteCategoryMemberByLessonId(id).orElseThrow(() -> {
            throw new LessonNotFoundException("해당하는 강의를 찾을 수 없습니다.");
        });
        checkAuthority(member, lesson);
        return lesson;
    }

    public Lesson plusPresentNumber(Member member, Long id, int count) {
        Lesson lesson = findLessonWithMember(id);
        checkAuthority(member, lesson);
        lesson.plusPresentNumber(count);
        return lesson;
    }

    public Lesson minusPresentNumber(Member member, Long id, int count) {
        Lesson lesson = findLessonWithMember(id);
        checkAuthority(member, lesson);
        lesson.minusPresentNumber(count);
        return lesson;
    }

    private Lesson findLessonWithMember(Long id) {
        return lessonRepository.findWithMemberByLessonId(id).orElseThrow(() -> new LessonNotFoundException("해당하는 강의를 찾을 수 없습니다."));
    }

    public Long saveLesson(Member member, LessonCreateDto.Request requestDto) {

        Site site = siteRepository.findById(requestDto.getSiteId())
                .orElseThrow( () -> new BusinessException("사이트가 존재하지 않습니다."));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow( () -> new BusinessException("카테고리가 존재하지 않습니다."));

        Lesson lesson = Lesson.builder()
                .lessonName(requestDto.getLessonName())
                .member(member)
                .alertDays(requestDto.getAlertDays())
                .totalNumber(requestDto.getTotalNumber())
                .price(requestDto.getPrice())
                .endDate(requestDto.getEndDate())
                .startDate(requestDto.getStartDate())
                .category(category)
                .message(requestDto.getMessage())
                .site(site)
                .build();

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
     * @param memberId
     * @param siteId
     * @param categoryId
     * @param lesson
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

        lesson.updateLesson(request.getLessonName(), request.getTotalNumber(), category, site);

        return lesson;
    }


    private void checkAuthority(Member member, Lesson lesson) {
        if (!StringUtils.equals(lesson.getMember().getMemberId(), member.getMemberId())) {
            throw new ForbiddenException("해당 강의에 대한 권한이 없습니다.");
        }
    }

    /**
     * 프론트에서 강의 등록 시 화면 그리는 순서 및 정보 반환
     * @param pageNumber
     * @return
     */
    public RenderOrderDto viewRenderOrder(int pageNumber) {
        RenderOrderDto renderOrderDto = new RenderOrderDto();
        List<RenderOrderDto.Response> lessonList = new ArrayList<>();

        if(pageNumber == 1){
            RenderOrderDto.Response lessonName = RenderOrderDto.Response.builder()
                    .key("lessonName")
                    .inputType("text")
                    .title("강의 이름")
                    .placeHolder("수강할 인강 이름을 입력해주세요")
                    .build();
            lessonList.add(lessonName);

            RenderOrderDto.Response totalNumber = RenderOrderDto.Response.builder()
                    .key("totalNumber")
                    .inputType("text")
                    .title("강의 개수")
                    .placeHolder("전체 강의 개수를 입력해주세요")
                    .build();
            lessonList.add(totalNumber);

            RenderOrderDto.Response category = RenderOrderDto.Response.builder()
                    .key("category")
                    .inputType("selectText")
                    .title("카테고리")
                    .placeHolder("인강 카테고리를 선택해주세요")
                    .build();
            lessonList.add(category);

            RenderOrderDto.Response site = RenderOrderDto.Response.builder()
                    .key("site")
                    .inputType("selectText")
                    .title("사이트")
                    .placeHolder("강의 사이트를 선택해주세요")
                    .build();
            lessonList.add(site);
        } else if(pageNumber == 2) {
            RenderOrderDto.Response startDate = RenderOrderDto.Response.builder()
                    .key("startDate")
                    .inputType("selectDate")
                    .title("강의 시작일")
                    .placeHolder(null)
                    .build();
            lessonList.add(startDate);

            RenderOrderDto.Response endDate = RenderOrderDto.Response.builder()
                    .key("endDate")
                    .inputType("selectDate")
                    .title("완강 목표일")
                    .placeHolder(null)
                    .build();
            lessonList.add(endDate);

            RenderOrderDto.Response price = RenderOrderDto.Response.builder()
                    .key("price")
                    .inputType("text")
                    .title("강의 금액")
                    .placeHolder("예: 10,000원")
                    .build();
            lessonList.add(price);

            RenderOrderDto.Response message = RenderOrderDto.Response.builder()
                    .key("message")
                    .inputType("text")
                    .title("각오 한 마디")
                    .placeHolder("최대 30자 까지 입력 가능합니다.")
                    .build();
            lessonList.add(message);
        }

        renderOrderDto.setLessonList(lessonList);

        return renderOrderDto;
    }
}