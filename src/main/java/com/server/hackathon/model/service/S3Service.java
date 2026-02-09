package com.server.hackathon.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.domain}")
    private String s3Domain;

    @Value("${aws.cloudfront.domain}")
    private String cloudFrontDomain;

    /**
     * READ (GET) - 파일 조회용 URL
     */
    public String getPresignedGetUrl(String folderName, String name, String extension) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(makeFileName(folderName, name, extension))
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // 유효 시간 10분
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        return replaceDomain(presignedRequest.url().toString());
    }

    /*
     * 2 & 3. CREATE & UPDATE (PUT) - 파일 업로드/수정용 URL
     * S3는 덮어쓰기가 기본이므로 생성과 수정은 동일한 PUT URL을 사용
     * 현재 기능은 적용하지 않고 추후 적용합니다
    public String getPresignedPutUrl(String fileName) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("application/octet-stream")
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        return replaceDomain(presignedRequest.url().toString());
    }
     */

    /**
     * 4. DELETE - 파일 삭제용 URL
     * 프론트엔드에서 바로 삭제 요청을 보낼 때 사용 => 추후 사용

    public String getPresignedDeleteUrl(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        DeleteObjectPresignRequest presignRequest = DeleteObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5)) // 삭제는 짧게 설정 권장
                .deleteObjectRequest(deleteObjectRequest)
                .build();

        PresignedDeleteObjectRequest presignedRequest = s3Presigner.presignDeleteObject(presignRequest);

        return replaceDomain(presignedRequest.url().toString());
    }
     */

    //S3 file name 생성
    private String makeFileName (String folderName, String name, String extension) {
        return folderName + "/" + name + "." + extension;
    }


    //S3 도메인을 CloudFront 도메인으로 교체
    private String replaceDomain(String originalUrl) {
        return originalUrl.replace(s3Domain, cloudFrontDomain);
    }
}
