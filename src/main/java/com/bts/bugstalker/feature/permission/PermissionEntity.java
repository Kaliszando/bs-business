package com.bts.bugstalker.feature.permission;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@Entity
@Table(name = "PERMISSION")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PermissionEntity {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_generator")
    @SequenceGenerator(name="permission_generator", sequenceName = "permission_seq")
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    private boolean adminPermission;
}
