package com.baseline.controller;

import com.baseline.domain.Member;
import com.baseline.common.Utils;
import com.baseline.common.result.SingleResult;
import com.baseline.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members/myself")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public SingleResult<Member.getDetails> getMember() {
        return new SingleResult<>(memberService.getMember(Utils.currentUser().getId()));
    }

    @PutMapping
    public SingleResult<Integer> updateMemberDetails(@RequestBody Member.UpdateDetails req) {
        int currentMemberId = Utils.currentUser().getId();
        memberService.updateMemberDetails(currentMemberId, req);

        return new SingleResult<>(currentMemberId);
    }

    @PostMapping("/withdraw")
    public SingleResult<Integer> withdraw() {
        Integer memberId = Utils.currentUser().getId();
        memberService.withdraw(memberId);
        return new SingleResult<>(memberId);
    }

//    @GetMapping("papers")
//    public PageResult<ConferencePaper.SelfListItemSimple> getMyPapers(@Valid ConferencePaper.ListReq req) {
//        Page<ConferencePaper.SelfListItemSimple> myPaperPage = memberService.getMyPapers(Utils.currentUser().getId(), req);
//
//        return PageMapper.toPageResult(myPaperPage);
//    }

    // 특정 컨퍼런스에 본인이 투고한 논문 모아보기
    // ConferencePaperController.getMyConferencePapers(...) @("/conferences/{conferenceId}/papers/my")와 동일
    // ConferencePaperRepository.findMyAllPaper(...) 재활용
//    @GetMapping("papers/{conferenceId}")
//    public PageResult<ConferencePaper.SelfListItemSimple> getMyConferencePapers(@Valid ConferencePaper.ListReq req, @PathVariable Integer conferenceId) {
//        Page<ConferencePaper.SelfListItemSimple> myConferencePaperPage = memberService.getMyConferencePapers(Utils.currentUser().getId(), conferenceId, req);
//
//        return PageMapper.toPageResult(myConferencePaperPage);
//    }
}
