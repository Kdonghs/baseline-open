package com.baseline.auth;

import com.baseline.common.errorcode.CommonErrorCode;
import com.baseline.common.exception.BadRequestException;
import com.baseline.entity.MemberEntity;
import com.baseline.repository.MemberRepository;
import com.baseline.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final LoginService loginService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        MemberEntity member = memberRepository.findByUid(username).orElseThrow(() -> new BadRequestException(CommonErrorCode.UNAUTHORIZED));
        return new AuthUser(member.getId());
    }

    public AuthUser login(String uid, String idToken) throws Exception {
        return new AuthUser(loginService.login(uid, idToken).getId());
    }
}
