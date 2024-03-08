package com.baseline.mapper;

import com.baseline.domain.Member;
import com.baseline.entity.MemberEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public void updateDetails(MemberEntity entity, Member.UpdateDetails update) {
        entity.setName(update.getName());
        entity.setBelongs(update.getBelongs());
        entity.setDepartment(update.getDepartment());
        entity.setPosition(update.getPosition());
        entity.setMobile(update.getMobile());
        entity.setOfficeNumber(update.getOfficeNumber());
    }
    public Member.getDetails getDetails(MemberEntity entity){
        return Member.getDetails.builder()
                .email(entity.getEmail())
                .conferenceEmail(entity.getConferenceEmail())
                .name(entity.getName())
                .mobile(entity.getMobile())
                .officeNumber(entity.getOfficeNumber())
                .belongs(entity.getBelongs())
                .department(entity.getDepartment())
                .department(entity.getDepartment())
                .build();
    }
}
