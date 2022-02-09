package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class FileUploadTestController {

    private final FileIOUtils fileIOUtils;

    @Autowired
    public FileUploadTestController(FileIOUtils fileIOUtils) {
        this.fileIOUtils = fileIOUtils;
    }

    @PostMapping("/stream/{fileUuid}")
    public ResponseEntity<?> uploadFileStream(@PathVariable String fileUuid, HttpServletRequest request) throws IOException {

        fileIOUtils.saveStreamFile(fileUuid,request);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/multipart/{fileUuid}")
    public ResponseEntity<?> uploadFileMultipart(
            @PathVariable String fileUuid,
            @RequestPart(value = "attachedFile", required=true) MultipartFile mpFile) throws IOException {

        fileIOUtils.saveMultipartFile(fileUuid, mpFile);
        return ResponseEntity.ok(true);
    }

}
