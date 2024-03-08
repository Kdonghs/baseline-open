package com.baseline.service;

import com.baseline.common.errorcode.MemberErrorCode;
import com.baseline.common.exception.AlreadyExistException;
import com.baseline.domain.Member;
import com.baseline.entity.MemberEntity;
import com.baseline.repository.MemberRepository;
import com.baseline.service.component.FirebaseAdminClient;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SignupService {
    private final MemberRepository memberRepository;
    private final FirebaseAdminClient firebaseAdminClient;

    public MemberEntity signup(Member.SignUpReq request) {
        if (memberRepository.findByUid(request.getUid()).isPresent()) {
            throw new AlreadyExistException(MemberErrorCode.ALREADY_EXIST);
        }

        FirebaseToken firebaseToken = firebaseAdminClient.getFirebaseUser(request.getIdToken());

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setName(request.getDisplayName());
        memberEntity.setUid(request.getUid());
        memberEntity.setEmail(firebaseToken.getEmail());
        memberEntity.setMobile(request.getMobile());
        memberEntity.setPhotoUrl(firebaseToken.getPicture());
        memberEntity.setProvider(request.getProvider());
        memberEntity.setLastAccessedAt(LocalDateTime.now());

        return memberRepository.save(memberEntity);
    }

    public UsernamePasswordAuthenticationToken getPrincipal(String clientId) {
        UserDetails user =  User.withUsername(clientId)
                .password("")
                .authorities(Collections.emptyList())
                .build();

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
