package com.baseline.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@Entity(name = "members")
@EntityListeners(AuditingEntityListener.class)
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128)
    private String email;
    private Boolean emailVerified = false;

    @Column(length = 128)
    private String conferenceEmail;
    private Boolean conferenceEmailVerified = false;

    @Column(length = 128)
    private String name;

    @Column(length = 32)
    private String mobile;

    @Column(length = 128)
    private String officeNumber;

    @Column(length = 128)
    private String belongs;

    @Column(length = 128)
    private String department;

    @Column(length = 128)
    private String position;

    @Column(length = 64)
    private String uid;

    @Column(length = 32)
    private String provider;

    @Column(length = 512)
    private String photoUrl;

    private LocalDateTime lastAccessedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
