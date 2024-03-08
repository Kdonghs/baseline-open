package com.baseline.service;

import com.baseline.entity.MemberEntity;
import com.baseline.repository.MemberRepository;
import com.baseline.service.component.FirebaseAdminClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final FirebaseAdminClient firebaseAdminClient;

    public MemberEntity login(String uid, String idToken) throws Exception {
        firebaseAdminClient.getFirebaseUser(idToken);

        return memberRepository.findByUid(uid).orElseThrow(() -> new Exception(""));
    }
}
