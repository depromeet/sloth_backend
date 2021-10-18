package com.sloth.domain.lesson;

import com.sloth.domain.BaseEntity;
import com.sloth.domain.category.Category;
import com.sloth.domain.member.Member;
import com.sloth.domain.site.Site;
import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

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
    public Lesson(Member member, String name, LocalDate startDate, LocalDate endDate,int totalNumber,
                  int price, String alertDays, Site site, Category category) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalNumber = totalNumber;
        this.price = price;
        this.alertDays = alertDays;
        this.site = site;
        this.category = category;

        this.isFinished = false;

        //connectMember(member);
    }

    private void connectMember(Member member) {
        this.member = member;
        member.getLessons().add(this);
    }

    public void plusPresentNumber(int count) {
        if (this.isFinished) {
            return;
        } else if (this.presentNumber + count >= this.totalNumber) {
            this.presentNumber = totalNumber;
            this.isFinished = true;
        }else {
            this.presentNumber += count; // TODO 주차별 진도율 반환
        }
    }

    public void minusPresentNumber(int count) {
        if (this.presentNumber == 0) {
            return;
        } else if (this.presentNumber - count <= 0) {
            this.presentNumber = 0;
            this.isFinished = false;
        }else {
            this.presentNumber -= count;
        }
    }

    public boolean isDoingLesson() {
        return this.getStartDate().isBefore(LocalDate.now()) && this.getEndDate().isAfter(LocalDate.now());
    }

    public int getRestDate() {
        return Period.between(this.getEndDate(), LocalDate.now()).getDays();
    }
}
