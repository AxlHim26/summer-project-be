package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.CandidateRequest;
import com.jobhunter.jobhunter_be.dto.response.CandidateResponse;
import com.jobhunter.jobhunter_be.entity.Candidate;
import com.jobhunter.jobhunter_be.entity.Profile;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.service.ICandidateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CandidateServiceImpl implements ICandidateService {
    private final UserRepository userRepository;

    @Override
    public CandidateResponse getCandidate(String email) throws NotFoundException {
        User user = userRepository.findUserWithCandidateAndProfile(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Candidate candidate = user.getCandidate();
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

        // Create Candidate if not exists
        if (candidate == null) {
            candidate = Candidate.builder()
                    .user(user)
                    .experience("")
                    .education("")
                    .skill("")
                    .build();
            user.setCandidate(candidate);
            userRepository.save(user);
        }

        return CandidateResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .bio(profile.getAbout())
                .address(profile.getAddress())
                .phone(profile.getPhone())
                .avatar(profile.getAvatar())
                .experience(candidate.getExperience())
                .education(candidate.getEducation())
                .skills(candidate.getSkill() != null && !candidate.getSkill().isEmpty()
                        ? List.of(candidate.getSkill().split(","))
                        : Collections.emptyList())
                .build();
    }

    @Override
    public CandidateResponse updateCandidate(CandidateRequest request, String email) throws NotFoundException {
        User user = userRepository.findUserWithCandidateAndProfile(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        Candidate candidate = user.getCandidate();
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

        // Create Candidate if not exists
        if (candidate == null) {
            candidate = Candidate.builder()
                    .user(user)
                    .experience("")
                    .education("")
                    .skill("")
                    .build();
            user.setCandidate(candidate);
        }

        // Update fields
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        profile.setAbout(request.getBio());
        profile.setAddress(request.getAddress());
        profile.setPhone(request.getPhone());
        profile.setAvatar(request.getAvatar());
        candidate.setExperience(request.getExperience());
        candidate.setEducation(request.getEducation());
        candidate.setSkill(request.getSkills() != null ? String.join(",", request.getSkills()) : "");

        userRepository.save(user);

        return CandidateResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .bio(profile.getAbout())
                .address(profile.getAddress())
                .phone(profile.getPhone())
                .avatar(profile.getAvatar())
                .experience(candidate.getExperience())
                .education(candidate.getEducation())
                .skills(candidate.getSkill() != null && !candidate.getSkill().isEmpty()
                        ? List.of(candidate.getSkill().split(","))
                        : Collections.emptyList())
                .build();
    }
}
