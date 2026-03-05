package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.JobApplicationRequest;
import com.jobhunter.jobhunter_be.dto.response.JobApplicationResponse;
import com.jobhunter.jobhunter_be.dto.response.CandidateApplicationResponse;
import com.jobhunter.jobhunter_be.dto.response.RecruiterApplicantResponse;
import com.jobhunter.jobhunter_be.dto.response.ApplicantDetailResponse;
import com.jobhunter.jobhunter_be.entity.*;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.repository.*;
import com.jobhunter.jobhunter_be.service.IJobApplicationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class JobApplicationServiceImpl implements IJobApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final StageRepository stageRepository;

    @Override
    public JobApplicationResponse applyToJob(String jobId, JobApplicationRequest request, String candidateEmail) throws NotFoundException {
        // Get job
        Integer jobIdInt = Integer.parseInt(jobId);
        Job job = jobRepository.findById(jobIdInt)
                .orElseThrow(() -> new NotFoundException("Job not found with id: " + jobId));

        // Check if job is not expired
        Date currentDate = new Date();
        if (job.getExperiedDate().before(currentDate)) {
            throw new NotFoundException("Job has expired");
        }

        // Get candidate
        User user = userRepository.findUserWithCandidateAndProfile(candidateEmail)
                .orElseThrow(() -> new NotFoundException("Candidate not found"));

        Candidate candidate = user.getCandidate();
        if (candidate == null) {
            throw new NotFoundException("Candidate profile not found");
        }

        // Check if already applied
        boolean alreadyApplied = applicationRepository.existsByCandidateAndJob(candidate, job);
        if (alreadyApplied) {
            throw new NotFoundException("You have already applied to this job");
        }

        // Get PENDING stage
        Stage pendingStage = stageRepository.findByProgress("PENDING")
                .orElseThrow(() -> new NotFoundException("PENDING stage not found"));

        // Create resume
        Resume resume = Resume.builder()
                .nameCandidate(request.getFullName())
                .emailCandidate(request.getEmail())
                .currentJob(request.getJobTitle())
                .portfolioLink(request.getPortfolioUrl() != null ? request.getPortfolioUrl() : "")
                .about(request.getAdditionalInfo() != null ? request.getAdditionalInfo() : "")
                .cv(request.getResume())
                .build();
        Resume savedResume = resumeRepository.save(resume);

        // Create application
        Application application = Application.builder()
                .candidate(candidate)
                .job(job)
                .resume(savedResume)
                .stage(pendingStage)
                .build();
        Application savedApplication = applicationRepository.save(application);

        return JobApplicationResponse.builder()
                .id(savedApplication.getId().toString())
                .jobId(job.getId().toString())
                .jobName(job.getJobName())
                .candidateName(request.getFullName())
                .candidateEmail(request.getEmail())
                .status("PENDING")
                .appliedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .build();
    }

    @Override
    public List<CandidateApplicationResponse> getCandidateApplications(String candidateEmail) throws NotFoundException {
        // Get candidate
        User user = userRepository.findUserWithCandidateAndProfile(candidateEmail)
                .orElseThrow(() -> new NotFoundException("Candidate not found"));

        Candidate candidate = user.getCandidate();
        if (candidate == null) {
            throw new NotFoundException("Candidate profile not found");
        }

        // Get all applications for this candidate
        List<Application> applications = applicationRepository.findByCandidateOrderByCreatedAtDesc(candidate);

        return applications.stream()
                .map(app -> {
                    Job job = app.getJob();
                    Recruiter recruiter = job.getRecruiter();
                    User recruiterUser = recruiter.getUser();
                    Stage stage = app.getStage();

                    return CandidateApplicationResponse.builder()
                            .id(app.getId().toString())
                            .jobId(job.getId().toString())
                            .jobName(job.getJobName())
                            .companyName(recruiterUser.getName()) // Using recruiter's name as company name
                            .location("Remote") // Default location, can be enhanced later
                            .jobType(job.getJobType())
                            .status(stage != null ? stage.getProgress() : "PENDING")
                            .appliedAt(new SimpleDateFormat("yyyy-MM-dd").format(app.getCreatedAt()))
                            .salary(job.getSalary().toString())
                            .category(job.getCategory())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasAppliedToJob(String jobId, String candidateEmail) throws NotFoundException {
        // Get candidate
        User user = userRepository.findUserWithCandidateAndProfile(candidateEmail)
                .orElseThrow(() -> new NotFoundException("Candidate not found"));

        Candidate candidate = user.getCandidate();
        if (candidate == null) {
            throw new NotFoundException("Candidate profile not found");
        }

        // Get job
        Integer jobIdInt = Integer.parseInt(jobId);
        Job job = jobRepository.findById(jobIdInt)
                .orElseThrow(() -> new NotFoundException("Job not found with id: " + jobId));

        // Check if already applied
        return applicationRepository.existsByCandidateAndJob(candidate, job);
    }

    @Override
    public List<RecruiterApplicantResponse> getRecruiterApplicants(String recruiterEmail) throws NotFoundException {
        // Get recruiter
        User recruiterUser = userRepository.findUserWithRecruiterAndProfile(recruiterEmail)
                .orElseThrow(() -> new NotFoundException("Recruiter not found"));

        Recruiter recruiter = recruiterUser.getRecruiter();
        if (recruiter == null) {
            throw new NotFoundException("Recruiter profile not found");
        }

        // Get all applications for jobs posted by this recruiter
        List<Application> applications = applicationRepository.findByJobRecruiterOrderByCreatedAtDesc(recruiter);

        return applications.stream()
                .map(app -> {
                    Job job = app.getJob();
                    Candidate candidate = app.getCandidate();
                    User candidateUser = candidate.getUser();
                    Profile candidateProfile = candidateUser.getProfile();
                    Resume resume = app.getResume();
                    Stage stage = app.getStage();

                    return RecruiterApplicantResponse.builder()
                            .applicationId(app.getId().toString())
                            .candidateId(candidate.getId().toString())
                            .candidateName(candidateUser.getName())
                            .candidateEmail(candidateUser.getEmail())
                            .candidatePhone(candidateProfile != null ? candidateProfile.getPhone() : "")
                            .jobId(job.getId().toString())
                            .jobName(job.getJobName())
                            .jobType(job.getJobType())
                            .salary(job.getSalary().toString())
                            .category(job.getCategory())
                            .status(stage != null ? stage.getProgress() : "PENDING")
                            .appliedAt(new SimpleDateFormat("yyyy-MM-dd").format(app.getCreatedAt()))
                            .resumeId(resume != null ? resume.getId().toString() : "")
                            .currentJob(resume != null ? resume.getCurrentJob() : "")
                            .portfolioLink(resume != null ? resume.getPortfolioLink() : "")
                            .about(resume != null ? resume.getAbout() : "")
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public ApplicantDetailResponse getApplicantDetail(String applicationId, String recruiterEmail) throws NotFoundException {
        // Get recruiter
        User recruiterUser = userRepository.findUserWithRecruiterAndProfile(recruiterEmail)
                .orElseThrow(() -> new NotFoundException("Recruiter not found"));

        Recruiter recruiter = recruiterUser.getRecruiter();
        if (recruiter == null) {
            throw new NotFoundException("Recruiter profile not found");
        }

        // Get application
        Long appId = Long.parseLong(applicationId);
        Application application = applicationRepository.findDetailById(appId)
                .orElseThrow(() -> new NotFoundException("Application not found"));

        // Verify this application belongs to recruiter's job
        if (!application.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new NotFoundException("Application not found");
        }

        Job job = application.getJob();
        Candidate candidate = application.getCandidate();
        User candidateUser = candidate.getUser();
        Profile candidateProfile = candidateUser.getProfile();
        Resume resume = application.getResume();
        Stage stage = application.getStage();

        return ApplicantDetailResponse.builder()
                .applicationId(application.getId().toString())
                .candidateId(candidate.getId().toString())
                .candidateName(candidateUser.getName())
                .candidateEmail(candidateUser.getEmail())
                .candidatePhone(candidateProfile != null ? candidateProfile.getPhone() : "")
                .jobId(job.getId().toString())
                .jobName(job.getJobName())
                .jobType(job.getJobType())
                .salary(job.getSalary().toString())
                .category(job.getCategory())
                .status(stage != null ? stage.getProgress() : "PENDING")
                .appliedAt(new SimpleDateFormat("yyyy-MM-dd").format(application.getCreatedAt()))
                .resumeId(resume != null ? resume.getId().toString() : "")
                .currentJob(resume != null ? resume.getCurrentJob() : "")
                .portfolioLink(resume != null ? resume.getPortfolioLink() : "")
                .about(resume != null ? resume.getAbout() : "")
                .experience(candidate.getExperience())
                .education(candidate.getEducation())
                .skills(candidate.getSkill())
                .address(candidateProfile != null ? candidateProfile.getAddress() : "")
                .avatar(candidateProfile != null ? candidateProfile.getAvatar() : "")
                .build();
    }
}
