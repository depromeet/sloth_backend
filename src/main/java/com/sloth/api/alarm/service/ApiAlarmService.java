package com.sloth.api.alarm.service;

import com.sloth.api.alarm.dto.AlarmSearchDto;
import com.sloth.domain.alarm.entity.Alarm;
import com.sloth.domain.alarm.service.AlarmService;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiAlarmService {

    private final MemberService memberService;
    private final AlarmService alarmService;

    public List<AlarmSearchDto.Response> getAlarmList(String email, AlarmSearchDto.Request requestDto) {
        Member member = memberService.findByEmail(email);
        Sort sort = Sort.by(Sort.Direction.DESC, "occurrenceTime");
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize(), sort);
        Page<Alarm> alarms = alarmService.findAlarms(member, pageable);
        if(alarms.hasContent()) {
            return alarms.getContent().stream()
                    .map(lesson -> AlarmSearchDto.Response.of(lesson))
                    .collect(Collectors.toList());
        }
        return null;
    }

}
