package com.sloth.infra.google;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.sloth.global.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class GoogleCloudStorageService {

    @Value("${google-cloud.project}")
    private String project;

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String credentialsLocation;

    private Storage storage;

    private final String[] IMAGE_EXTENTIONS = {"jpeg", "png", "jpg"};

    @PostConstruct
    public void GcsServiceInitialize() throws IOException {
        InputStream keyFile = ResourceUtils.getURL(credentialsLocation).openStream();
        storage = StorageOptions.newBuilder().setProjectId(project)
                // Key 파일 수동 등록
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build().getService();
    }

    /**
     * GCS 파일 업로드
     */
    public String uploadImageFileToGCS(MultipartFile file, String bucketName) {
        String savedFileName;
        UUID uuid = UUID.randomUUID();
        try {
            Bucket bucket = storage.get(bucketName);
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // 이미지 파일인지 확장자 검사
            if(isImageFile(extension)) {
                throw new BusinessException("이미지 유형의 파일을 업로드해주세요(jpg, jpeg, png)");
            }

            savedFileName = uuid + extension;
            bucket.create(savedFileName, file.getBytes(), file.getContentType());
        } catch (Exception e) {
            log.info("파일 업로드 중 오류가 발생하였습니다.", e);
            throw new BusinessException("파일 업로드 중 오류가 발생하였습니다.");
        }
        return savedFileName;
    }

    private boolean isImageFile(String extension) {
        boolean isImageFile = false;
        for(String imageExtention : IMAGE_EXTENTIONS) {
            if(imageExtention.equals(extension)) {
                isImageFile = true;
                break;
            }
        }
        return isImageFile;
    }

    /**
     * GCS 파일 다운로드 기능
     * @param googleCloudStorageDownloadDto
     * @return
     * @throws IOException
     */
    public void downloadFileFromGCS(GoogleCloudStorageDownloadDto googleCloudStorageDownloadDto) {
        try {
            Blob blob = storage.get(googleCloudStorageDownloadDto.getBucketName(),
                    googleCloudStorageDownloadDto.getDownloadFileName());
            blob.downloadTo(Paths.get(googleCloudStorageDownloadDto.getLocalFileLocation()));
        } catch (Exception e) {
            log.info("파일 다운로드 중 오류가 발생하였습니다.", e);
            throw new BusinessException("파일 다운로드 중 오류가 발생하였습니다.");
        }
    }

}