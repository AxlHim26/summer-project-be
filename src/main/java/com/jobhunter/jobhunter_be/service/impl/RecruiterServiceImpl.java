package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.RecruiterRequest;
import com.jobhunter.jobhunter_be.dto.response.RecruiterResponse;
import com.jobhunter.jobhunter_be.entity.Profile;
import com.jobhunter.jobhunter_be.entity.Recruiter;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.service.IRecruiterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RecruiterServiceImpl implements IRecruiterService {
    private final UserRepository userRepository;

    @Override
    public RecruiterResponse getRecruiter(String email) throws NotFoundException {
        User user = userRepository.findUserWithRecruiterAndProfile(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Recruiter recruiter = user.getRecruiter();
        Profile profile = user.getProfile();

        return RecruiterResponse.builder()
                .name(user.getName())
                .avatar(profile.getAvatar())
                .location(recruiter.getLocation() != null
                        ? List.of(recruiter.getLocation().split(","))
                        : Collections.emptyList())
                .employee(recruiter.getEmployee())
                .industry(recruiter.getIndustry())
                .techStack(recruiter.getTechStack() != null
                        ? List.of(recruiter.getTechStack().split(","))
                        : Collections.emptyList())
                .foundedDate(recruiter.getFoundedDate())
                .description(profile.getAbout())
                .build();
    }

    @Override
    public RecruiterResponse updateRecruiter(RecruiterRequest request, String email) throws NotFoundException {
        User user = userRepository.findUserWithRecruiterAndProfile(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Recruiter recruiter = user.getRecruiter();
        Profile profile = user.getProfile();

        user.setName(request.getName());
        profile.setAbout(request.getDescription());
        profile.setAvatar(request.getAvatar());
        recruiter.setLocation(request.getLocation() != null ? String.join(",", request.getLocation()) : null);
        recruiter.setTechStack(request.getTechStack() != null ? String.join(",", request.getTechStack()) : null);
        recruiter.setEmployee(request.getEmployee());
        recruiter.setIndustry(request.getIndustry());
        recruiter.setFoundedDate(request.getFoundedDate());

        userRepository.save(user);

        return RecruiterResponse.builder()
                .avatar(profile.getAvatar())
                .name(user.getName())
                .location(recruiter.getLocation() != null
                        ? List.of(recruiter.getLocation().split(","))
                        : Collections.emptyList())
                .employee(recruiter.getEmployee())
                .industry(recruiter.getIndustry())
                .techStack(recruiter.getTechStack() != null
                        ? List.of(recruiter.getTechStack().split(","))
                        : Collections.emptyList())
                .foundedDate(recruiter.getFoundedDate())
                .description(user.getProfile().getAbout())
                .build();
    }
}
