package com.example.demo.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class DataURLController {
    //이미지가 있는 폴더 경로
    private String imageDir="c:/web/image_Semi/";//팀 공유 폴더가..

    //Get 요청 처리,파일 이름 받음
    @GetMapping("/image/{save_name}")
    public ResponseEntity<Resource> showImage(@PathVariable("save_name") String save_name)
            throws MalformedURLException {
        // 안전하게 경로 검사
        if (save_name.contains("..")) {
            return ResponseEntity.badRequest().build();
        }

        Path filePath = Paths.get(imageDir + save_name);

        // 파일 없으면 404
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        // Content-Type 자동 설정
        String contentType;
        try {
            contentType = Files.probeContentType(filePath);
        } catch (Exception e) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}