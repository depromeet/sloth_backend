package com.sloth.domain.lesson;

import com.sloth.domain.category.Category;
import com.sloth.domain.common.BaseEntity;
import com.sloth.domain.lesson.constant.LessonStatus;
import com.sloth.domain.member.Member;
import com.sloth.domain.site.Site;
import com.sloth.global.exception.ForbiddenException;
import com.sloth.global.exception.InvalidParameterException;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.thymeleaf.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@AuditOverride(forClass=BaseEntity.class)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Getter
@AllArgsConstructor @NoArgsConstructor
@ToString(exclude = {"category", "member", "site"})
@EqualsAndHashCode(of = "lessonId", callSuper = false)
@Table(name = "lesson")
public class Lesson extends BaseEntity  {

    @Id @GeneratedValue
    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(nullable = false, length = 150)
    private String lessonName;

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

    @Column(nullable = true)
    private String alertDays;

    @Column(length = 200)
    private String message;

    @Column(nullable = false)
    private Boolean isFinished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Lesson(Long lessonId, Member member, String lessonName, LocalDate startDate, LocalDate endDate, int totalNumber,
                  int presentNumber, int price, String alertDays, String message, Site site, Category category) {
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalNumber = totalNumber;
        this.presentNumber = presentNumber;
        this.price = price;
        this.alertDays = alertDays;
        this.site = site;
        this.category = category;
        this.message = message;
        this.isFinished = false;

        connectMember(member);
    }

    public void verifyOwner(Member member) {
        if (!StringUtils.equals(this.getMember().getMemberId(), member.getMemberId())) {
            throw new ForbiddenException("이 강의에 대한 권한이 없습니다.");
        }
    }
    private void connectMember(Member member) {
        this.member = member;
        if(member != null) {
            member.getLessons().add(this);
        }
    }

    public void updatePresentNumber(int count) {
        if (this.isFinished) {
            return;
        } else {
            this.presentNumber += count;
        }

        if (this.presentNumber >= totalNumber) {
            this.presentNumber = totalNumber;
        } else if (this.presentNumber <= 0) {
            this.presentNumber = 0;
        }
    }

    public boolean isDoingLesson(LocalDate now) {
        return ((this.getStartDate().isBefore(now) && this.getEndDate().isAfter(now)) ||
                (this.getStartDate().isEqual(now) || this.getEndDate().isEqual(now)));
    }

    private int getPastDays(LocalDate now) {
        Long days = (Long) ChronoUnit.DAYS.between(this.getStartDate(), now) + 1;
        return days.intValue();
    }

    private int getTotalDays() {
        Long days = (Long) ChronoUnit.DAYS.between(this.getStartDate(), this.getEndDate()) + 1;
        return days.intValue();
    }

    public int getRemainDay(LocalDate now) {
        Long days = (Long) ChronoUnit.DAYS.between(now, this.endDate);
        return days.intValue();
    }

    public int getCurrentProgressRate() {
        return (int) Math.floor((double) presentNumber / (double) totalNumber * 100);
    }

    public int getGoalProgressRate(LocalDate now) {
        if(now.isBefore(startDate)) {
            return 0;
        }

        int goalProgressRate = (int) Math.floor( (double) getGoalNumber(now) / (double) getTotalNumber() * 100);
        return goalProgressRate <= 100 ? goalProgressRate : 100;
    }

    public int getWastePrice(LocalDate now) {
        int wastePrice = (int) (price * ((double) (getGoalProgressRate(now) - getCurrentProgressRate()) / (double) 100));
        return wastePrice > 0 ? wastePrice : 0;
    }

    public LessonStatus getLessonStatus(LocalDate now) {
        LessonStatus lessonStatus;
        if( now.isAfter(endDate) || this.isFinished == true ){
            lessonStatus = LessonStatus.PAST;
        } else if(now.isBefore(startDate)) {
            lessonStatus = LessonStatus.PLAN;
        } else {
            lessonStatus = LessonStatus.CURRENT;
        }
        return lessonStatus;
    }

    public void updateLesson(String lessonName, Integer totalNumber, int price, Category category, Site site) {
        this.lessonName = lessonName;
        this.totalNumber = totalNumber;
        this.category = category;
        this.price = price;
        this.site = site;
    }

    public int getGoalNumber(LocalDate now) {
        return (int) Math.ceil((((double)getPastDays(now)/(double)getTotalDays()) * (double) getTotalNumber()));
    }

    public void updateDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new InvalidParameterException("종료 일자는 시작 일자 이후여야 합니다.");
        }
        //TODO 내부적인 정책 필요
        this.startDate = startDate;
        this.endDate = endDate;
    }

     public void updateCategory(Category category) {
        this.category = category;
     }

     public void updateSite(Site site) {
        this.site = site;
     }

     public void finishLesson() {
        this.isFinished = true;
     }
}
