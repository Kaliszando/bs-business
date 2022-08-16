package com.bts.bugstalker.core.user;

import com.bts.bugstalker.core.common.audit.AuditBaseEntity;
import com.bts.bugstalker.core.common.enums.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;

@Getter @Setter @Builder
@Entity @Table(name = "USER")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends AuditBaseEntity implements UserDetails {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "user_seq")
    private Long id;

    @NotBlank @NotNull private String username;

    @NotBlank @NotNull private String firstName;

    @NotBlank @NotNull private String lastName;

    @NotBlank @NotNull private String email;

    @NotBlank @NotNull private String password;

    @Enumerated(EnumType.STRING) @NotNull
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getCode()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
