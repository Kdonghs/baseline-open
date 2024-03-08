package com.baseline.service;

import com.baseline.repository.MemberRepository;
import com.baseline.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ConferenceServiceTest {
    private ConferenceService conferenceService;

    private ConferenceRepository conferenceRepository = Mockito.mock(ConferenceRepository.class);
    private MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    private RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    private ConferenceAdminRepository conferenceAdminRepository = Mockito.mock(ConferenceAdminRepository.class);

    private ConferenceMapper conferenceMapper = new ConferenceMapper();

    @BeforeEach
    void init() {
        conferenceService = new ConferenceService(
                conferenceRepository, memberRepository, roleRepository, conferenceAdminRepository, conferenceMapper
        );
    }

    @Test
    @DisplayName("상세 컨퍼런스 반환")
    void getConference() {
        //        given
        ConferenceEntity entity = new ConferenceEntity();
        entity.setId(2);
        entity.setName("test1");
        entity.setDescription("test1");
        entity.setCode("test");
        entity.setYear("test");
        entity.setVenue("test");

        entity.setAdmins(new ArrayList<ConferenceAdminEntity>());

        when(conferenceRepository.findById(anyInt())).thenReturn(Optional.of(entity));

        //        when
        Conference.Details result = conferenceService.getConference(2);

        //        then
        assertEquals("test1", result.getName());
    }
}