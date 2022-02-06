package com.sloth.api.fcm.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(url = "https://fcm.googleapis.com/v1/projects/test-454ab/messages:send", name = "FcmLessonPushFeignClient")
public interface FcmLessonPushFeignClient {

    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    String lessonPushAlarm(
            @RequestHeader("Content-type") String contentType,
            @RequestHeader("Authorization") String accessToken,
            @RequestBody String message
    );
}
