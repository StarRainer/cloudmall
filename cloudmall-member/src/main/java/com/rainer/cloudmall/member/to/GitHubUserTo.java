package com.rainer.cloudmall.member.to;

import lombok.Data;

@Data
public class GitHubUserTo {
    private Long id;
    private String name;
    private String email;
    private String avatarUrl;
    private String bio;
    private String accessToken;
}
