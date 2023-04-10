package com.sloth.api.alarm.controller;

import com.sloth.api.alarm.dto.AlarmSearchDto;
import com.sloth.api.alarm.dto.AlarmSearchV2Dto;
import com.sloth.api.alarm.service.ApiAlarmService;
import com.sloth.global.dto.PageResponseDto;
import com.sloth.global.exception.BusinessException;
import com.sloth.global.exception.ErrorCode;
import com.sloth.global.resolver.CurrentEmail;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/alarms")
@RestController
@RequiredArgsConstructor
public class ApiAlarmController {

    private final ApiAlarmService apiAlarmService;

    @Deprecated
    @GetMapping
    @Operation(summary = "알람 목록 조회 API", description = "알람 목록 리스트 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<List<AlarmSearchDto.Response>> getAlarmList(@CurrentEmail String email, AlarmSearchDto.Request requestDto) {
        List<AlarmSearchDto.Response> alarmSearchResponses = apiAlarmService.getAlarmList(email, requestDto);
        return ResponseEntity.ok(alarmSearchResponses);
    }

    @GetMapping("/v2")
    @Operation(summary = "알람 목록 조회 V2 API", description = "알람 목록 리스트 조회 V2 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public PageResponseDto<List<AlarmSearchV2Dto.Response>> getPageAlarmList(@CurrentEmail String email, AlarmSearchDto.Request requestDto) {
        PageResponseDto<List<AlarmSearchV2Dto.Response>> pageAlarmSearchResponseDtos = apiAlarmService.getPageAlarmList(email, requestDto);
        return pageAlarmSearchResponseDtos;
    }

    @PatchMapping("/{alarmId}/read-time")
    @Operation(summary = "알람 읽은 시간 업데이트 API", description = "알람 읽은 시간 업데이트 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<String> updateAlarmReadTime(@CurrentEmail String email, @PathVariable Long alarmId) {
        apiAlarmService.updateAlarmReadTime(email, alarmId);
        return ResponseEntity.ok("success");
    }


}
