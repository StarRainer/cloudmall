package com.rainer.cloudmall.authentication.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitHubUserTo {
    private Long id;
    private String name;
    private String email;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String bio;
    private String accessToken;
}
