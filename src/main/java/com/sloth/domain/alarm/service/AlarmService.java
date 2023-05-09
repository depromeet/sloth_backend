package com.sloth.domain.alarm.service;

import com.sloth.domain.alarm.entity.Alarm;
import com.sloth.domain.alarm.repository.AlarmRepository;
import com.sloth.domain.member.Member;
import com.sloth.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public Page<Alarm> findAlarms(Member member, Pageable pageable) {
        return alarmRepository.findByMember(member, pageable);
    }

    public Alarm getAlarmByMemberAndAlarmId(Member member, Long alarmId) {
        return alarmRepository.findByMemberAndAlarmId(member, alarmId)
                .orElseThrow(() -> new BusinessException("해당 회원의 알람 데이터가 존재하지 않습니다."))
                ;
    }

    public void deleteAllAlarm(Member member) {
        List<Alarm> alarmList = alarmRepository.findByMember(member);
        alarmList.stream().forEach(alarm -> alarmRepository.deleteById(alarm.getAlarmId()));
    }
}
