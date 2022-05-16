package com.bts.bugstalker.core.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity create(String roleName) {
        return roleRepository.save(RoleEntity.builder()
                .name(roleName)
                .build()
        );
    }

}
