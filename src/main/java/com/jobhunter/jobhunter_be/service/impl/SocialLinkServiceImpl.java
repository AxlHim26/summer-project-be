package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.SocialLinkRequest;
import com.jobhunter.jobhunter_be.dto.response.SocialLinkResponse;
import com.jobhunter.jobhunter_be.entity.SocialLink;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.repository.SocialLinkRepository;
import com.jobhunter.jobhunter_be.service.ISocialLinkService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SocialLinkServiceImpl implements ISocialLinkService {

    private final SocialLinkRepository socialLinkRepository;

    @Override
    public SocialLinkResponse getSocialLinkByEmail(String email) throws NotFoundException {
        SocialLink socialLink = socialLinkRepository.findSocialLinkByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("SocialLink not found"));

        return SocialLinkResponse.builder()
                .facebookLink(socialLink.getFacebookLink())
                .linkedinLink(socialLink.getLinkedinLink())
                .twitterLink(socialLink.getTwitterLink())
                .build();
    }

    @Override
    public SocialLinkResponse updateSocialLinkByEmail(SocialLinkRequest request, String email) throws NotFoundException {
        SocialLink socialLink = socialLinkRepository.findSocialLinkByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("SocialLink not found"));

        socialLink.setFacebookLink(request.getFacebookLink());
        socialLink.setTwitterLink(request.getTwitterLink());
        socialLink.setLinkedinLink(request.getLinkedinLink());

        SocialLink saved = socialLinkRepository.save(socialLink);
        return SocialLinkResponse.builder()
                .facebookLink(saved.getFacebookLink())
                .linkedinLink(saved.getLinkedinLink())
                .twitterLink(saved.getTwitterLink())
                .build();
    }
}
