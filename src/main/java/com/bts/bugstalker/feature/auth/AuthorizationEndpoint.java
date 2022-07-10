package com.bts.bugstalker.feature.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthorizationEndpoint {

    @GetMapping("/admin")
    public ResponseEntity<String> admin(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello world");
    }

    @GetMapping("/secured")
    public ResponseEntity<String> secured() {
        return ResponseEntity.ok("Hello secured");
    }

    @PostMapping("/api/v1/auth/sign-in")
    public void signIn(@RequestBody LoginCredentials credentials) {
        if(credentials != null) {
            System.out.println("dupa");
        }
    }

}
