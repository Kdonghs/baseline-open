package com.baseline.entity;

import com.baseline.enums.RoleType;
import com.baseline.enums.RoleName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity(name = "roles")
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private RoleType type;

    @Column(length = 128)
    private String description;
}
