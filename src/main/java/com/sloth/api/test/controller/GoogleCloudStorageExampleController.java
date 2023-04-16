package com.sloth.api.test.controller;

import com.sloth.infra.google.GoogleCloudStorageDownloadDto;
import com.sloth.infra.google.GoogleCloudStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/api/google-cloud-storage/profiles")
@RequiredArgsConstructor
public class GoogleCloudStorageExampleController {

    @Value("${google-cloud.profile-bucket-name}")
    private String profileBucketName;

    private final GoogleCloudStorageService googleCloudStorageService;

    @PostMapping("/download")
    public ResponseEntity localDownloadFromStorage(@RequestBody GoogleCloudStorageDownloadDto googleCloudStorageDownloadDto) {
        googleCloudStorageService.downloadFileFromGCS(googleCloudStorageDownloadDto);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/upload")
    public ResponseEntity uploadProfileImage(MultipartFile file) {
        googleCloudStorageService.uploadImageFileToGCS(file, profileBucketName);
        return ResponseEntity.ok("success");
    }

}
