package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.SocialLinkRequest;
import com.jobhunter.jobhunter_be.dto.response.SocialLinkResponse;
import com.jobhunter.jobhunter_be.entity.Profile;
import com.jobhunter.jobhunter_be.entity.SocialLink;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.repository.SocialLinkRepository;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.security.CustomUserDetails;
import com.jobhunter.jobhunter_be.service.ISocialLinkService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SocialLinkServiceImpl implements ISocialLinkService {

    private final SocialLinkRepository socialLinkRepository;
    private final UserRepository userRepository;

    @Override
    public SocialLinkResponse getSocialLinkByEmail(String email) throws NotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            user.setProfile(profile);
            userRepository.save(user);
        }

        if (profile.getSocialLink() == null) {
            SocialLink socialLink = new SocialLink();
            profile.setSocialLink(socialLink);
            userRepository.save(user);
        }

        SocialLink socialLink = profile.getSocialLink();

        return SocialLinkResponse.builder()
                .facebookLink(socialLink.getFacebookLink())
                .linkedinLink(socialLink.getLinkedinLink())
                .twitterLink(socialLink.getTwitterLink())
                .build();
    }


    @Override
    public SocialLinkResponse updateSocialLinkByEmail(SocialLinkRequest request, String email) throws NotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            user.setProfile(profile);
            userRepository.save(user);
        }

        if (profile.getSocialLink() == null) {
            SocialLink socialLink = new SocialLink();
            profile.setSocialLink(socialLink);
            userRepository.save(user);
        }
        SocialLink socialLink = user.getProfile().getSocialLink();

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
