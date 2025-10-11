package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.RecruiterRequest;
import com.jobhunter.jobhunter_be.dto.response.RecruiterResponse;
import com.jobhunter.jobhunter_be.entity.Profile;
import com.jobhunter.jobhunter_be.entity.Recruiter;
import com.jobhunter.jobhunter_be.entity.SocialLink;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.repository.SocialLinkRepository;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.service.IRecruiterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

@Service
@Slf4j
@AllArgsConstructor
public class RecruiterServiceImpl implements IRecruiterService {
    private final UserRepository userRepository;
    private final SocialLinkRepository socialLinkRepository;

    @Override
    public RecruiterResponse getRecruiter(String email) throws NotFoundException {
        User user = userRepository.findUserWithRecruiterAndProfile(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Recruiter recruiter = user.getRecruiter();
        Profile profile = user.getProfile();

        // Create Profile if not exists
        if (profile == null) {
            profile = Profile.builder()
                    .avatar("")
                    .about("")
                    .address("")
                    .phone("")
                    .build();
            user.setProfile(profile);
        }

        // Create Recruiter if not exists
        if (recruiter == null) {
            recruiter = Recruiter.builder()
                    .user(user)
                    .website("")
                    .location("")
                    .employee("")
                    .industry("")
                    .techStack("")
                    .benefit("")
                    .build();
            user.setRecruiter(recruiter);
            userRepository.save(user);
        }

        // Get SocialLink for contact information
        SocialLink socialLink = socialLinkRepository.findSocialLinkByUserEmail(email).orElse(null);
        if (socialLink == null) {
            // Create default SocialLink if not exists
            socialLink = SocialLink.builder()
                    .user(user)
                    .twitterLink("")
                    .facebookLink("")
                    .linkedinLink("")
                    .build();
            socialLinkRepository.save(socialLink);
        }

        return RecruiterResponse.builder()
                .name(user.getName())
                .avatar(profile.getAvatar())
                .website(recruiter.getWebsite())
                .location(recruiter.getLocation() != null && !recruiter.getLocation().isEmpty()
                        ? List.of(recruiter.getLocation().split(","))
                        : Collections.emptyList())
                .employee(recruiter.getEmployee())
                .industry(recruiter.getIndustry())
                .techStack(recruiter.getTechStack() != null && !recruiter.getTechStack().isEmpty()
                        ? List.of(recruiter.getTechStack().split(","))
                        : Collections.emptyList())
                .foundedDate(recruiter.getFoundedDate() != null 
                    ? new SimpleDateFormat("yyyy-MM-dd").format(recruiter.getFoundedDate()) 
                    : null)
                .description(profile.getAbout())
                .benefit(recruiter.getBenefit())
                // Contact information from SocialLink
                .twitter(socialLink.getTwitterLink())
                .facebook(socialLink.getFacebookLink())
                .linkedin(socialLink.getLinkedinLink())
                .instagram("") // Not available in current entity
                .email(user.getEmail())
                .build();
    }

    @Override
    public RecruiterResponse updateRecruiter(RecruiterRequest request, String email) throws NotFoundException {
        User user = userRepository.findUserWithRecruiterAndProfile(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Recruiter recruiter = user.getRecruiter();
        Profile profile = user.getProfile();

        // Create Profile if not exists
        if (profile == null) {
            profile = Profile.builder()
                    .avatar("")
                    .about("")
                    .address("")
                    .phone("")
                    .build();
            user.setProfile(profile);
        }

        // Create Recruiter if not exists
        if (recruiter == null) {
            recruiter = Recruiter.builder()
                    .user(user)
                    .website("")
                    .location("")
                    .employee("")
                    .industry("")
                    .techStack("")
                    .benefit("")
                    .build();
            user.setRecruiter(recruiter);
        }

        // Update fields
        user.setName(request.getName());
        profile.setAbout(request.getDescription());
        profile.setAvatar(request.getAvatar());
        recruiter.setWebsite(request.getWebsite());
        recruiter.setLocation(request.getLocation() != null ? String.join(",", request.getLocation()) : "");
        recruiter.setTechStack(request.getTechStack() != null ? String.join(",", request.getTechStack()) : "");
        recruiter.setEmployee(request.getEmployee());
        recruiter.setIndustry(request.getIndustry());
        // Convert String to Date for entity
        if (request.getFoundedDate() != null && !request.getFoundedDate().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                recruiter.setFoundedDate(sdf.parse(request.getFoundedDate()));
            } catch (ParseException e) {
                log.error("Error parsing founded date: {}", request.getFoundedDate(), e);
                recruiter.setFoundedDate(null);
            }
        } else {
            recruiter.setFoundedDate(null);
        }
        recruiter.setBenefit(request.getBenefit());

        userRepository.save(user);

        return RecruiterResponse.builder()
                .avatar(profile.getAvatar())
                .name(user.getName())
                .website(recruiter.getWebsite())
                .location(recruiter.getLocation() != null && !recruiter.getLocation().isEmpty()
                        ? List.of(recruiter.getLocation().split(","))
                        : Collections.emptyList())
                .employee(recruiter.getEmployee())
                .industry(recruiter.getIndustry())
                .techStack(recruiter.getTechStack() != null && !recruiter.getTechStack().isEmpty()
                        ? List.of(recruiter.getTechStack().split(","))
                        : Collections.emptyList())
                .foundedDate(recruiter.getFoundedDate() != null 
                    ? new SimpleDateFormat("yyyy-MM-dd").format(recruiter.getFoundedDate()) 
                    : null)
                .description(profile.getAbout())
                .benefit(recruiter.getBenefit())
                .build();
    }
}
