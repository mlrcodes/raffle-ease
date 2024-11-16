package com.raffleease.raffles_service.S3.Controllers;

import com.raffleease.raffles_service.S3.Services.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/s3")
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/get-upload-urls")
    public ResponseEntity<List<String>> getUploadUrls(
            @RequestBody Map<String, List<String>> request
    ) {
        return ResponseEntity.ok(s3Service.generateUploadUrls(request.get("fileNames")));
    }

    @PostMapping("/get-view-urls")
    public ResponseEntity<List<String>> getViewUrls(
            @RequestBody Map<String, List<String>> request
    ) {
        return ResponseEntity.ok(s3Service.generateViewUrls(request.get("fileNames")));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(
            @RequestParam(value = "file") String key
    ) {
        s3Service.delete(key);
        return ResponseEntity.ok().build();
    }
}