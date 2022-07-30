package com.bts.bugstalker.core.member;

import com.bts.bugstalker.core.project.ProjectEntity;
import com.bts.bugstalker.core.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter @Setter @Builder
@Entity @Table(name = "MEMBERSHIP")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MembershipEntity {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "membership_generator")
    @SequenceGenerator(name="membership_generator", sequenceName = "membership_seq")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
