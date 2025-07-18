package com.jobhunter.jobhunter_be.controller;

import com.jobhunter.jobhunter_be.dto.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<RestResponse<String>> test() {
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.success("test","day la du lieu test"));
    }
}
