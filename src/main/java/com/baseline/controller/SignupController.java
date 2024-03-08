package com.baseline.controller;

import com.baseline.domain.Member;
import com.baseline.service.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SignupController {
    private final SignupService signupService;
    private final TokenEndpoint tokenEndpoint;

    @PostMapping("/signup")
    public Member.OAuthRes signUp(@Valid @RequestBody Member.SignUpReq request) throws Exception {
        log.info("Signup {}", request);

        signupService.signup(request);
        return new Member.OAuthRes(Objects.requireNonNull(tokenEndpoint.getAccessToken(
//              firebase 수정
                signupService.getPrincipal("baseline"),
                request.toMap()
        ).getBody()));
    }
}
