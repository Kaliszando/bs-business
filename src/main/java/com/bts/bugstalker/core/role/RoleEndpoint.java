package com.bts.bugstalker.core.role;

import com.bts.bugstalker.config.AppInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(AppInfoProvider.API_V1_PATH)
public class RoleEndpoint {

    private final RoleService roleService;

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/role")
    public String createRole(@RequestBody String roleName) {
        return roleService.create(roleName).getName();
    }

}
