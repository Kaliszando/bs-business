package com.bts.bugstalker.feature.permission;

import com.bts.bugstalker.common.enums.Permission;
import com.bts.bugstalker.common.enums.UserRole;
import com.bts.bugstalker.core.context.ContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PermissionManager {

    private final PermissionService permissionService;

    private final ContextProvider contextProvider;

    public boolean contextUserHasPermission(Permission permission) {
        return contextProvider.getUser().getRole().equals(UserRole.ADMIN);
//        return permissionService.has(contextProvider.getUser().getId(), permission);
    }
}
