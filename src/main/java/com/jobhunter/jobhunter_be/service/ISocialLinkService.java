package com.jobhunter.jobhunter_be.service;

import com.jobhunter.jobhunter_be.dto.request.SocialLinkRequest;
import com.jobhunter.jobhunter_be.dto.response.SocialLinkResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;

public interface ISocialLinkService {
    SocialLinkResponse getSocialLinkByEmail(String email) throws NotFoundException;

    SocialLinkResponse updateSocialLinkByEmail(SocialLinkRequest request, String email) throws NotFoundException;
}
