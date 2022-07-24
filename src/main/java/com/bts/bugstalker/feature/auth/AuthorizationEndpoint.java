package com.bts.bugstalker.feature.auth;

import com.bts.bugstalker.api.AuthApi;
import com.bts.bugstalker.api.model.LoginCredentialsDto;
import com.bts.bugstalker.util.parameters.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(ApiPaths.V1)
public class AuthorizationEndpoint implements AuthApi {

    @GetMapping("/admin")
    public ResponseEntity<String> admin(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }

    @GetMapping("/secured")
    public ResponseEntity<String> secured() {
        return ResponseEntity.ok("Hello secured");
    }

    @Override
    public ResponseEntity<Void> ping() {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> signIn(@Valid LoginCredentialsDto request) {
        return ResponseEntity.noContent().build();
    }
}
