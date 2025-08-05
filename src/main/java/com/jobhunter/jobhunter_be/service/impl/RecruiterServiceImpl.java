package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.RecruiterRequest;
import com.jobhunter.jobhunter_be.dto.response.RecruiterResponse;
import com.jobhunter.jobhunter_be.entity.Profile;
import com.jobhunter.jobhunter_be.entity.Recruiter;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.repository.RecruiterRepository;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.security.CustomUserDetails;
import com.jobhunter.jobhunter_be.service.IRecruiterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RecruiterServiceImpl implements IRecruiterService {
    private final RecruiterRepository recruiterRepository;
    private final UserRepository userRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public RecruiterResponse getRecruiter(String email) throws NotFoundException {
        User user = userRepository.findByEmail(email) .orElseThrow(() -> new NotFoundException("User not found"));
        Recruiter recruiter = user.getRecruiter();
        if (recruiter == null) {
            recruiter = new Recruiter();
            recruiter.setUser(user);
            user.setRecruiter(recruiter);
            userRepository.save(user);
        }

        if(user.getProfile() == null){
            user.setProfile(new Profile());
            userRepository.save(user);
        }

        return RecruiterResponse.builder()
                .name(user.getName())
                .location(List.of(recruiter.getLocation() != null ? recruiter.getLocation().split(",") : new String[]{}))
                .employee(recruiter.getEmployee())
                .industry(recruiter.getIndustry())
                .techStack(List.of(recruiter.getTechStack() != null ? recruiter.getTechStack().split(",") : new String[]{}))
                .foundedDate(recruiter.getFoundedDate() != null
                        ? dateFormat.format(recruiter.getFoundedDate())
                        : null)
                .description(user.getProfile().getAbout())
                .build();
    }

    @Override
    public RecruiterResponse updateRecruiter(RecruiterRequest request, String email) throws NotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        Recruiter recruiter = user.getRecruiter();
        if (recruiter == null) {
            recruiter = new Recruiter();
            recruiter.setUser(user);
            user.setRecruiter(recruiter);
        }

        if (user.getProfile() == null) {
            user.setProfile(new Profile());
        }

        user.setName(request.getName());
        recruiter.setLocation(String.join(",", request.getLocation()));
        recruiter.setEmployee(request.getEmployee());
        recruiter.setIndustry(request.getIndustry());
        recruiter.setTechStack(String.join(",", request.getTechStack()));
        user.getProfile().setAbout(request.getDescription());


        try {
            if (request.getFoundedDate() != null) {
                Date parsedDate = dateFormat.parse(request.getFoundedDate());
                recruiter.setFoundedDate(parsedDate);
            }
        } catch (ParseException e) {
            throw new RuntimeException("Invalid foundedDate format, expected yyyy-MM-dd", e);
        }

        recruiterRepository.save(recruiter);

        return RecruiterResponse.builder()
                .name(user.getName())
                .location(List.of(recruiter.getLocation() != null ? recruiter.getLocation().split(",") : new String[]{}))
                .employee(recruiter.getEmployee())
                .industry(recruiter.getIndustry())
                .techStack(List.of(recruiter.getTechStack() != null ? recruiter.getTechStack().split(",") : new String[]{}))
                .foundedDate(recruiter.getFoundedDate() != null
                        ? dateFormat.format(recruiter.getFoundedDate())
                        : null)
                .description(user.getProfile().getAbout())
                .build();
    }
}
