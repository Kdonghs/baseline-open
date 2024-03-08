package com.baseline.service;

import com.baseline.common.errorcode.MemberErrorCode;
import com.baseline.common.exception.NotFoundException;
import com.baseline.domain.Member;
import com.baseline.entity.MemberEntity;
import com.baseline.mapper.MemberMapper;
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
public class MemberService {
    private final MemberRepository memberRepository;
//    private final ConferencePaperRepository conferencePaperRepository;

    private final FirebaseAdminClient firebaseAdminClient;

    private final MemberMapper memberMapper;
//    private final ConferencePaperMapper conferencePaperMapper;

    public Member.getDetails getMember(int memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(MemberErrorCode.NOT_FOUND)
        );

        return memberMapper.getDetails(memberEntity);
    }

    public void updateMemberDetails(int currentMemberId, Member.UpdateDetails req) {
        MemberEntity memberEntity = memberRepository.findById(currentMemberId).orElseThrow(
                () -> new NotFoundException(MemberErrorCode.NOT_FOUND)
        );

        memberMapper.updateDetails(memberEntity, req);
        memberRepository.save(memberEntity);
    }

    // 회원탈퇴
    public void withdraw(int currentMemberId){
        MemberEntity memberEntity = memberRepository.findById(currentMemberId).orElseThrow(
                () -> new NotFoundException(MemberErrorCode.NOT_FOUND)
        );

        // 회원탈퇴는 추후 구현
    }
}
