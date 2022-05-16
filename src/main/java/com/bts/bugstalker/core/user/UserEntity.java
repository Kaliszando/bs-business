package com.bts.bugstalker.core.user;

import com.bts.bugstalker.core.role.RoleEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter @Builder
@Entity
@Table(name = "USER")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank @NotNull
    private String username;

    @NotBlank @NotNull
    private String firstName;

    @NotBlank @NotNull
    private String lastName;

    @NotBlank @NotNull
    private String email;

    @NotBlank @NotNull
    private String password;

    @ManyToMany
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID",
                    foreignKey = @ForeignKey(name = "FK_USER_ROLE__USER_ID")),

            inverseJoinColumns = @JoinColumn(name = "ROLE_ID",
                    foreignKey = @ForeignKey(name = "FK_USER_ROLE__ROLE_ID")))
    private List<RoleEntity> roles;

}