package com.sloth.domain.alarm.repository;

import com.sloth.domain.alarm.entity.Alarm;
import com.sloth.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Page<Alarm> findByMember(Member member, Pageable pageable);

    Optional<Alarm> findByMemberAndAlarmId(Member member, Long alarmId);

}
