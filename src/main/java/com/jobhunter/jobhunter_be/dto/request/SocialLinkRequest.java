package com.jobhunter.jobhunter_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialLinkRequest {
    private String facebookLink;
    private String twitterLink;
    private String linkedinLink;
}
