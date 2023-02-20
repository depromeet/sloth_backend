package com.sloth.domain.alarm.entity;

import com.sloth.domain.alarm.constant.AlarmType;
import com.sloth.domain.common.BaseEntity;
import com.sloth.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "alarmId")
@Table(name = "alarm")
public class Alarm extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long alarmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlarmType alarmType;

    @Column(length = 300)
    private String url; // 클릭 시 이동할 url

    private Long alarmContentId; // 알림을 발생시킨 글의 pk

    @Column(length = 200)
    private String title; // 알림 제목

    @Column(length = 500)
    private String message; // 알림 메세지

    @Column(nullable = false)
    private LocalDateTime occurrenceTime; // 알람 발생 시간

    private LocalDateTime readTime; // 알림 확인 일시

    public void updateReadTime(LocalDateTime readTime) {
        this.readTime = readTime;
    }

}
