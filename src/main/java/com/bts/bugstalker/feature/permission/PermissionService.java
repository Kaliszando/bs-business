package com.bts.bugstalker.feature.permission;

import com.bts.bugstalker.common.enums.Permission;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public boolean has(Long userId, Permission permission) {
        return true;
    }
}
