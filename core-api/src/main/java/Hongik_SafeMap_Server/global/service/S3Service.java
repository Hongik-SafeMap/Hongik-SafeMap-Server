package Hongik_SafeMap_Server.global.service;

import Hongik_SafeMap_Server.global.dto.request.PresignedUrlRequest;
import Hongik_SafeMap_Server.global.dto.response.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.upload.expiration-minutes:15}")
    private int expirationMinutes;

    @Value("${aws.s3.region}")
    private String region;

    public PresignedUrlResponse generatePresignedUrl(PresignedUrlRequest request) {
        return generatePresignedUrl(request, "uploads");
    }

    public PresignedUrlResponse generatePresignedUrl(PresignedUrlRequest request, String folder) {
        String uniqueFileName = generateUniqueFileName(request.getFileName(), folder);
        Duration expiration = Duration.ofMinutes(expirationMinutes);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileName)
                .contentType(request.getContentType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(expiration)
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        String presignedUrl = presignedRequest.url().toString();

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes);
        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, uniqueFileName);

        log.info("Generated presigned URL for file: {} with fileUrl: {}", request.getFileName(), fileUrl);

        return new PresignedUrlResponse(presignedUrl, fileUrl, expiresAt);
    }

    private String generateUniqueFileName(String originalFileName, String folder) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex);
        }

        return String.format("%s/%s_%s%s", folder, timestamp, uuid, extension);
    }
}
