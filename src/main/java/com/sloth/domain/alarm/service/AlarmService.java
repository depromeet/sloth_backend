package com.sloth.domain.alarm.service;

import com.sloth.domain.alarm.entity.Alarm;
import com.sloth.domain.alarm.repository.AlarmRepository;
import com.sloth.domain.member.Member;
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

}
