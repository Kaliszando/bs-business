package com.bts.bugstalker.core.permission;

import com.bts.bugstalker.core.shared.enums.Permission;
import com.bts.bugstalker.core.shared.enums.UserRole;
import com.bts.bugstalker.feature.context.ContextProvider;
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
