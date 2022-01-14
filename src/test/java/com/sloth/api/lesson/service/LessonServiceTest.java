package com.sloth.api.lesson.service;

import com.sloth.creator.LessonCreator;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.nickname.service.NicknameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @InjectMocks LessonService lessonService;

    @Mock MemberRepository memberRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock NicknameService nicknameService;

    @Test
    @DisplayName("lesson 객체 remainDay 기준으로 정렬 테스트")
    void sortByRemainDay() {
        //given
        List<Lesson> lessons = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 4; i++) {
            Lesson lesson = LessonCreator.createLesson("test" + i, now, now.plusDays(10 - i), null, null, 10, 100);
            lessons.add(lesson);
        }
        //when
        lessonService.sortByRemainDay(lessons);
        //then
        assertThat(lessons.get(0).getRemainDay(now)).isLessThanOrEqualTo(lessons.get(1).getRemainDay(now));
        assertThat(lessons.get(0).getLessonName()).isEqualTo("test3");
    }

}