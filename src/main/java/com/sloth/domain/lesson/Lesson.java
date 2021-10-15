package com.sloth.domain.lesson;

import com.sloth.domain.BaseEntity;
import com.sloth.domain.category.Category;
import com.sloth.domain.member.Member;
import com.sloth.domain.site.Site;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor
@ToString(exclude = "member")
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "lesson")
public class Lesson extends BaseEntity  {

    @Id @GeneratedValue
    @Column(name = "lesson_id")
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    private int presentNumber;

    private int totalNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String alertDays;

    @Column(length = 200)
    private String message;

    @Column(nullable = false)
    private Boolean isFinished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Lesson(Member member, String name, LocalDate startDate, LocalDate endDate, int price, String alertDays) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.alertDays = alertDays;

        this.isFinished = false;

        connectMember(member);
    }

    private void connectMember(Member member) {
        this.member = member;
        member.getLessons().add(this);
    }

    public void plusPresentNumber() {
        if (this.isFinished) {
            return;
        }
        this.presentNumber++; // TODO 주차별 진도율 반환
        checkLessonFinished();
    }

    private void checkLessonFinished() {
        if (this.presentNumber == this.totalNumber) {
            this.isFinished = true;
        }
    }

    public void minusPresentNumber() {
        if (this.presentNumber == 0) {
            return;
        }
        this.presentNumber--;
    }
}
