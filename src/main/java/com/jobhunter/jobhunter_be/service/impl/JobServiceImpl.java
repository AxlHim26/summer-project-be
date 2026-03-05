package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.JobRequest;
import com.jobhunter.jobhunter_be.dto.response.JobResponse;
import com.jobhunter.jobhunter_be.entity.Job;
import com.jobhunter.jobhunter_be.entity.Recruiter;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.repository.JobRepository;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.service.IJobService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class JobServiceImpl implements IJobService {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @Override
    public JobResponse createJob(JobRequest request, String email) throws NotFoundException {
        User user = userRepository.findUserWithRecruiterAndProfile(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Recruiter recruiter = user.getRecruiter();
        if (recruiter == null) {
            throw new NotFoundException("Recruiter profile not found");
        }

        // Calculate expired date (30 days from now)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date expiredDate = calendar.getTime();

        // Create Job entity
        Job job = Job.builder()
                .recruiter(recruiter)
                .jobName(request.getJobTitle())
                .jobType(String.join(",", request.getEmploymentTypes()))
                .description(request.getJobDescription())
                .salary(request.getSalaryMax()) // Use max salary as primary salary
                .category(String.join(",", request.getCategories()))
                .requireSkill(String.join(",", request.getRequiredSkills()))
                .whoAreYou(request.getQualifications())
                .reponsibility(request.getResponsibilities())
                .niceToHave(request.getNiceToHaves())
                .capacity(1) // Default capacity
                .experiedDate(expiredDate)
                .build();

        Job savedJob = jobRepository.save(job);

        return JobResponse.builder()
                .id(savedJob.getId().toString())
                .jobName(savedJob.getJobName())
                .jobType(savedJob.getJobType())
                .description(savedJob.getDescription())
                .salary(savedJob.getSalary().toString())
                .category(savedJob.getCategory())
                .requireSkill(savedJob.getRequireSkill())
                .whoAreYou(savedJob.getWhoAreYou())
                .reponsibility(savedJob.getReponsibility())
                .niceToHave(savedJob.getNiceToHave())
                .capacity(savedJob.getCapacity())
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").format(savedJob.getCreateAt()))
                .expiredDate(new SimpleDateFormat("yyyy-MM-dd").format(savedJob.getExperiedDate()))
                .status("published")
                .build();
    }

    @Override
    public JobResponse[] getJobsByRecruiter(String email) throws NotFoundException {
        User user = userRepository.findUserWithRecruiterAndProfile(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Recruiter recruiter = user.getRecruiter();
        if (recruiter == null) {
            throw new NotFoundException("Recruiter profile not found");
        }

        List<Job> jobs = jobRepository.findByRecruiterOrderByCreateAtDesc(recruiter);

        return jobs.stream()
                .map(job -> JobResponse.builder()
                        .id(job.getId().toString())
                        .jobName(job.getJobName())
                        .jobType(job.getJobType())
                        .description(job.getDescription())
                        .salary(job.getSalary().toString())
                        .category(job.getCategory())
                        .requireSkill(job.getRequireSkill())
                        .whoAreYou(job.getWhoAreYou())
                        .reponsibility(job.getReponsibility())
                        .niceToHave(job.getNiceToHave())
                        .capacity(job.getCapacity())
                        .createdAt(new SimpleDateFormat("yyyy-MM-dd").format(job.getCreateAt()))
                        .expiredDate(new SimpleDateFormat("yyyy-MM-dd").format(job.getExperiedDate()))
                        .status("published")
                        .build())
                .toArray(JobResponse[]::new);
    }

    @Override
    public JobResponse[] getAllAvailableJobs() {
        List<Job> availableJobs = jobRepository.findByExperiedDateAfterOrderByCreateAtDesc(new Date());

        return availableJobs.stream()
                .map(job -> JobResponse.builder()
                        .id(job.getId().toString())
                        .jobName(job.getJobName())
                        .jobType(job.getJobType())
                        .description(job.getDescription())
                        .salary(job.getSalary().toString())
                        .category(job.getCategory())
                        .requireSkill(job.getRequireSkill())
                        .whoAreYou(job.getWhoAreYou())
                        .reponsibility(job.getReponsibility())
                        .niceToHave(job.getNiceToHave())
                        .capacity(job.getCapacity())
                        .createdAt(new SimpleDateFormat("yyyy-MM-dd").format(job.getCreateAt()))
                        .expiredDate(new SimpleDateFormat("yyyy-MM-dd").format(job.getExperiedDate()))
                        .status("published")
                        .build())
                .toArray(JobResponse[]::new);
    }

    @Override
    public JobResponse getJobById(String jobId) throws NotFoundException {
        Integer id = Integer.parseInt(jobId);
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job not found with id: " + jobId));

        // Check if job is not expired
        Date currentDate = new Date();
        if (job.getExperiedDate().before(currentDate)) {
            throw new NotFoundException("Job has expired");
        }

        return JobResponse.builder()
                .id(job.getId().toString())
                .jobName(job.getJobName())
                .jobType(job.getJobType())
                .description(job.getDescription())
                .salary(job.getSalary().toString())
                .category(job.getCategory())
                .requireSkill(job.getRequireSkill())
                .whoAreYou(job.getWhoAreYou())
                .reponsibility(job.getReponsibility())
                .niceToHave(job.getNiceToHave())
                .capacity(job.getCapacity())
                .createdAt(new SimpleDateFormat("yyyy-MM-dd").format(job.getCreateAt()))
                .expiredDate(new SimpleDateFormat("yyyy-MM-dd").format(job.getExperiedDate()))
                .status("published")
                .build();
    }
}
