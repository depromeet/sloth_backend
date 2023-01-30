package com.sloth.api.alarm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sloth.domain.alarm.constant.AlarmType;
import com.sloth.domain.alarm.entity.Alarm;
import com.sloth.global.dto.PageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class AlarmSearchDto {

    @Getter
    @ApiModel(value = "회원 알람 리스트 조회 객체",description = "회원 알람 리스트 조회 객체")
    public static class Request extends PageDto {

    }

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        @ApiModelProperty(value = "알람 아이디")
        private Long alarmId;

        @ApiModelProperty(value = "알람 타입 : LESSON (강의), ANNOUNCEMENT(공지사항)")
        private String alarmType;

        @ApiModelProperty(value = "알람 클릭시 이동할 url")
        private String url;

        @ApiModelProperty(value = "알림을 발생시킨 글의 pk")
        private Long alarmContentId;

        @ApiModelProperty(value = "알림 메세지")
        private String message;

        @ApiModelProperty(value = "알람 발생 시간")
        @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
        private LocalDateTime occurrenceTime;

        @ApiModelProperty(value = "알림 확인 일시")
        @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
        private LocalDateTime readTime;

        public static AlarmSearchDto.Response of(Alarm alarm) {
            return Response.builder()
                    .alarmId(alarm.getAlarmId())
                    .alarmType(alarm.getAlarmType().name())
                    .url(alarm.getUrl())
                    .alarmContentId(alarm.getAlarmContentId())
                    .message(alarm.getMessage())
                    .occurrenceTime(alarm.getOccurrenceTime())
                    .readTime(alarm.getReadTime())
                    .build();
        }
    }

}
