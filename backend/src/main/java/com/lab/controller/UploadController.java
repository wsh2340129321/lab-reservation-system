package com.lab.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private static final String UPLOAD_DIR = Paths.get("").toAbsolutePath().toString() + File.separator + "uploads" + File.separator;

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "请选择要上传的文件");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFilename = UUID.randomUUID().toString() + extension;

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File destFile = new File(UPLOAD_DIR + newFilename);
            file.transferTo(destFile);

            String imageUrl = "/uploads/" + newFilename;
            response.put("success", true);
            response.put("data", imageUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "文件上传失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/laboratory/{id}/image")
    public ResponseEntity<?> uploadLaboratoryImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "请选择要上传的文件");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFilename = "lab_" + id + "_" + UUID.randomUUID().toString() + extension;

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File destFile = new File(UPLOAD_DIR + newFilename);
            file.transferTo(destFile);

            String imageUrl = "/uploads/" + newFilename;
            response.put("success", true);
            response.put("data", imageUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "文件上传失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}