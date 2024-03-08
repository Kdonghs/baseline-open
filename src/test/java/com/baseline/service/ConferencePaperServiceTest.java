package com.baseline.service;

import com.baseline.entity.MemberEntity;
import com.baseline.file.FileEntity;
import com.baseline.file.FileRepository;
import com.baseline.file.StorageService;
import com.baseline.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ConferencePaperServiceTest {
    private ConferencePaperService conferencePaperService;

    private StorageService storageService = Mockito.mock(StorageService.class);

    private ConferencePaperRepository conferencePaperRepository = Mockito.mock(ConferencePaperRepository.class);
    private MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    private ConferenceRepository conferenceRepository = Mockito.mock(ConferenceRepository.class);
    private FileRepository fileRepository = Mockito.mock(FileRepository.class);
    private ConferencePaperFileRepository conferencePaperFileRepository = Mockito.mock(ConferencePaperFileRepository.class);

    private ConferencePaperMapper conferencePaperMapper = new ConferencePaperMapper();

    @BeforeEach
    void init(){
        conferencePaperService = new ConferencePaperService(
                storageService,conferencePaperRepository,memberRepository,conferenceRepository,
                fileRepository,conferencePaperFileRepository,conferencePaperMapper
        );
    }

//    conferencePaper를 조회할 때 conferencePaperId와 memberId를 같이 검증하면 코드 생략 가능
//    conferencePaper의 conferenceID를 검증하는 부분이 중복 됨
//    soft delete 여부 검증 필요
    @Test
    @DisplayName("1번")
    void getConferencePaper() {
        //        given
        MemberEntity member = new MemberEntity();
        member.setId(1);

        ConferenceEntity paper = new ConferenceEntity();
        paper.setId(1);

        ConferencePaperEntity entity = new ConferencePaperEntity();
        entity.setMember(member);
        entity.setConference(paper);
        entity.setTitle("test");

        when(conferencePaperRepository.findById(anyInt())).thenReturn(Optional.of(entity));

        //        when
        ConferencePaper.SimpleLookUp result = conferencePaperService.getConferencePaper(1,1,1);

        //        then
        assertEquals("test", result.getTitle());
    }

    @Test
    @DisplayName("2번")
    void createConferencePaper() {
        //        given
        ConferencePaper.Create req = new ConferencePaper.Create();
        req.setPaperFileId(1);

        when(memberRepository.findById(anyInt())).thenReturn(Optional.of(new MemberEntity()));
        when(conferenceRepository.findById(anyInt())).thenReturn(Optional.of(new ConferenceEntity()));
        when(fileRepository.findByIdAndMemberId(anyInt(),anyInt())).thenReturn(Optional.of(new FileEntity()));

        //        when
        int result = conferencePaperService.createConferencePaper(req,1,1);

        //        then
        assertNull(result);
        //id는 자동 생성됨, 따라서 null
    }

    @Test
    @DisplayName("3번")
    void uploadConferencePaperFile() {
        //        given
        MultipartFile file = null;

        when(memberRepository.findById(anyInt())).thenReturn(Optional.of(new MemberEntity()));
        when(conferenceRepository.findById(anyInt())).thenReturn(Optional.of(new ConferenceEntity()));
        when(fileRepository.findByIdAndMemberId(anyInt(),anyInt())).thenReturn(Optional.of(new FileEntity()));

        //        when
        int result = conferencePaperService.uploadConferencePaperFile(file,1,1);

        //        then
        assertEquals(0,result);
    }

//    conferencePaper를 조회할 때 conferencePaperId와 memberId를 같이 검증하면 코드 생략 가능
//    soft delete 여부 검증 필요
    @Test
    @DisplayName("4번")
    void updateConferencePaper() {
        //        given
        ConferencePaper.Create req = new ConferencePaper.Create();

        MemberEntity member = new MemberEntity();
        member.setId(1);

        ConferencePaperEntity conferencePaper = new ConferencePaperEntity();
        conferencePaper.setMember(member);

        when(conferencePaperRepository.findByIdAndConferenceId(anyInt(),anyInt())).thenReturn(Optional.of(conferencePaper));
        //        when
        int result = conferencePaperService.updateConferencePaper(1,1,1,req);

        //        then
        //수정 요청 시간을 set하지 못해서 불가
    }
//    conferencePaper를 조회할 때 conferencePaperId와 memberId를 같이 검증하면 코드 생략 가능
//    soft delete 여부 검증 필요
    @Test
    @DisplayName("5번")
    void deleteConferencePaper() {
        //        given
        MemberEntity member = new MemberEntity();
        member.setId(1);

        ConferenceEntity conference = new ConferenceEntity();
        conference.setPaperReceiptEndDate(LocalDateTime.of(2023,11,11,11,11));

        ConferencePaperEntity conferencePaper = new ConferencePaperEntity();
        conferencePaper.setId(1);
        conferencePaper.setMember(member);
        conferencePaper.setConference(conference);

        when(conferencePaperRepository.findByIdAndConferenceId(anyInt(),anyInt())).thenReturn(Optional.of(conferencePaper));

        //        when
        int result = conferencePaperService.deleteConferencePaper(1,1,1);
        //        then
        assertEquals(1,result);
    }
}