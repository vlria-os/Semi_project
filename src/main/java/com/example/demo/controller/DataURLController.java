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
    private String imageDir="192.168.0.37/image_Semi/";//팀 공유 폴더가..

    @GetMapping("/image/{save_name}")
    public UrlResource showImage(@PathVariable("save_name") String save_name)
            throws MalformedURLException {
        return new UrlResource("file:"+imageDir+save_name);
    }

}