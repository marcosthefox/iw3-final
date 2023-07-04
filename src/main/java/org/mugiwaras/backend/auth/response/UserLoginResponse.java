package org.mugiwaras.backend.auth.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserLoginResponse {
    private String token;
    private String matriz;
}
