package com.baseline.file;

import com.baseline.entity.MemberEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "files")
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = MemberEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Column(length = 128)
    private String originalFileName;

    @Column(length = 512)
    private String fileName; // Url

    @Column(length = 64)
    private String fileExt;

    @CreatedDate
    private LocalDateTime createdAt;
}
