package com.bts.bugstalker.feature.auth;

import com.bts.bugstalker.util.parameters.ApiPaths;
import org.apache.commons.lang3.NotImplementedException;
import org.openapitools.api.AuthApi;
import org.openapitools.model.LoginCredentialsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiPaths.V1)
public class AuthEndpoint implements AuthApi {

    @Override
    public ResponseEntity<Void> ping() {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> signIn(@Valid LoginCredentialsDto request) {
        // proxied handling to LoginAuthFilter
        throw new NotImplementedException("sign-in endpoint not implemented");
    }

    @Override
    public ResponseEntity<Void> signOut() {
        // proxied handling to LogoutSuccessHandler
        throw new NotImplementedException("sign-out endpoint not implemented");
    }
}
