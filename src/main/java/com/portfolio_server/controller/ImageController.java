package com.portfolio_server.controller;

import com.portfolio_server.service.ImageStorageService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageStorageService storageService;

    /**
     * Admin: upload an image and get back its absolute URL, which the editor
     * embeds into the project content.
     */
    @PostMapping
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {   // POST로 업로드된 요청 데이터 중 "file"이란 이름의 파일을 꺼내옴
        String path = storageService.store(file);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()   //현재 요청이 들어온 서버 root 주소 localhost:8080로 옮
                .path(path) /* /uploads/abc.png */
                .toUriString(); //localhost:8080/uploads/abc.png로 붙여서 만듦
        return Map.of("url", url);  //key: value로 만듦
    }
}