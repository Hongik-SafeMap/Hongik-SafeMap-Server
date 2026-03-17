package Hongik_SafeMap_Server.global.controller;

import Hongik_SafeMap_Server.global.dto.request.PresignedUrlRequest;
import Hongik_SafeMap_Server.global.dto.response.PresignedUrlResponse;
import Hongik_SafeMap_Server.global.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "파일 업로드", description = "파일 업로드 관련 API")
@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final S3Service s3Service;

    @Operation(summary = "이미지 업로드용 Presigned URL 생성", description = "S3 이미지 파일 업로드를 위한 presigned URL을 생성합니다. presignedUrl에 반환 받은 imageURL을 업로드 해주세요. (presigned URL은 15분 후 자동으로 만료됩니다.)")
    @PostMapping("/upload-url")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PresignedUrlResponse> generateUploadUrl(@Valid @RequestBody PresignedUrlRequest request) {
        PresignedUrlResponse response = s3Service.generatePresignedUrl(request);
        return ResponseEntity.ok(response);
    }
}