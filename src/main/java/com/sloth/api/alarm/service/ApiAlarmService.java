package com.sloth.api.alarm.service;

import com.sloth.api.alarm.dto.AlarmSearchDto;
import com.sloth.api.alarm.dto.AlarmSearchV2Dto;
import com.sloth.domain.alarm.entity.Alarm;
import com.sloth.domain.alarm.service.AlarmService;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.global.constant.DefaultPageSize;
import com.sloth.global.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiAlarmService {

    private final MemberService memberService;
    private final AlarmService alarmService;

    @Deprecated
    public List<AlarmSearchDto.Response> getAlarmList(String email, AlarmSearchDto.Request requestDto) {
        Member member = memberService.findByEmail(email);
        Sort sort = Sort.by(Sort.Direction.DESC, "occurrenceTime");
        Pageable pageable = PageRequest.of(requestDto.getPage(), DefaultPageSize.SIZE_TEN.getSize(), sort);
        Page<Alarm> alarms = alarmService.findAlarms(member, pageable);
        if(alarms.hasContent()) {
            return alarms.getContent().stream()
                    .map(lesson -> AlarmSearchDto.Response.of(lesson))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 알람 리스트 페이징 조회
     */
    public PageResponseDto<List<AlarmSearchV2Dto.Response>> getPageAlarmList(String email, AlarmSearchDto.Request requestDto) {
        Member member = memberService.findByEmail(email);
        Sort sort = Sort.by(Sort.Direction.DESC, "occurrenceTime");
        Pageable pageable = PageRequest.of(requestDto.getPage()-1, DefaultPageSize.SIZE_TEN.getSize(), sort);
        Page<Alarm> alarms = alarmService.findAlarms(member, pageable);

        if(alarms.hasContent()) {

            List<AlarmSearchV2Dto.Response> alarmSearchResponseDtos = alarms.getContent().stream()
                    .map(lesson -> AlarmSearchV2Dto.Response.of(lesson))
                    .collect(Collectors.toList());

            return PageResponseDto.<List<AlarmSearchV2Dto.Response>>builder()
                    .totalItems(alarms.getTotalElements())
                    .totalPages(alarms.getTotalPages())
                    .currentPage(pageable.getPageNumber())
                    .itemsPerPage(pageable.getPageSize())
                    .data(alarmSearchResponseDtos)
                    .build();
        }

        return PageResponseDto.<List<AlarmSearchV2Dto.Response>>builder()
                .totalItems(alarms.getTotalElements())
                .totalPages(alarms.getTotalPages())
                .currentPage(pageable.getPageNumber())
                .itemsPerPage(pageable.getPageSize())
                .data(null)
                .build();
    }

    @Transactional
    public void updateAlarmReadTime(String email, Long alarmId) {
        Member member = memberService.findByEmail(email);
        Alarm alarm = alarmService.getAlarmByMemberAndAlarmId(member, alarmId);
        alarm.updateReadTime(LocalDateTime.now());
    }

}
