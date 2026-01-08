package com.example.demo.controller;

import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
public class DataURLController {
    private String imageDir="c:/image_Semi/";

    @GetMapping("/image/{save_name}")
    public UrlResource showImage(@PathVariable("save_name") String save_name)
            throws MalformedURLException {
        return new UrlResource("file:"+imageDir+save_name);
    }
}
