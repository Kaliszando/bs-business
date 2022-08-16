package com.bts.bugstalker.core.group;

import com.bts.bugstalker.core.permission.PermissionEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@Table(name = "GROUP_")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupEntity {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_generator")
    @SequenceGenerator(name="group_generator", sequenceName = "group_seq")
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    private boolean defaultGroup;

    @ManyToMany
    @JoinTable(
            name = "GROUP_PERMISSION",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionEntity> permissions;
}
