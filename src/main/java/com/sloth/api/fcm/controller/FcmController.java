package com.sloth.api.fcm.controller;

import com.sloth.api.fcm.dto.FcmDto;
import com.sloth.api.fcm.dto.FcmTokenDto;
import com.sloth.api.fcm.dto.FcmTokenUpdateDto;
import com.sloth.api.fcm.service.FirebaseCloudMessageService;
import com.sloth.global.resolver.CurrentEmail;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/api/fcm")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<String> pushMessage(@RequestBody FcmDto.Request requestDTO) throws IOException {
        firebaseCloudMessageService.sendMessageTo(
                requestDTO.getTargetToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok("success");
    }

    @PostMapping("/api/fcmtoken")
    public ResponseEntity<String> saveFcmToken(@CurrentEmail String email, @RequestBody FcmTokenDto.Request request) {
        firebaseCloudMessageService.saveFcmToken(email, request.getFcmToken());
        return ResponseEntity.ok("success");
    }

    @PatchMapping("/api/fcmtoken/use")
    public ResponseEntity<FcmTokenUpdateDto.Response> updateFcmTokenUse(@CurrentEmail String email, @RequestBody FcmTokenUpdateDto.Request request) {
        FcmTokenUpdateDto.Response response = firebaseCloudMessageService.updateFcmTokenUse(email, request);
        return ResponseEntity.ok(response);
    }

}
