package com.bts.bugstalker.core.permission;

import com.bts.bugstalker.core.shared.enums.Permission;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public boolean has(Long userId, Permission permission) {
        return true;
    }
}
