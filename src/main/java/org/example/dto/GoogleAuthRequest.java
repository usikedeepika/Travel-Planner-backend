package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GoogleAuthRequest {
    private String token;
    private GoogleUserData userData;
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class GoogleUserData{
        private String email;
        @JsonProperty("given_name")
        private String firstName;
        @JsonProperty("family_name")
        private String lastName;
        private String username;
    }
}