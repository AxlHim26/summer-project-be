package com.jobhunter.jobhunter_be.controller;

import com.jobhunter.jobhunter_be.dto.common.RestResponse;
import com.jobhunter.jobhunter_be.dto.request.SocialLinkRequest;
import com.jobhunter.jobhunter_be.dto.response.SocialLinkResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.service.impl.SocialLinkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
public class SocialLinkController {
    private final SocialLinkServiceImpl socialLinkService;

    @GetMapping("/setting/social-link")
    public ResponseEntity<RestResponse<SocialLinkResponse>> getSocialLinks(Authentication authentication) throws NotFoundException {
        SocialLinkResponse response = socialLinkService.getSocialLinkByEmail(authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Social links fetched successfully"));
    }

    @PutMapping("/setting/social-link")
    public ResponseEntity<RestResponse<SocialLinkResponse>> updateSocialLinks(@RequestBody SocialLinkRequest request, Authentication authentication) throws NotFoundException {
        SocialLinkResponse response = socialLinkService.updateSocialLinkByEmail(request,authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Social links updated successfully"));
    }
}
