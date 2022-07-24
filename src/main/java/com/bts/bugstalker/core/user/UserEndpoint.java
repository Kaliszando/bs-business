package com.bts.bugstalker.core.user;

import com.bts.bugstalker.api.UserApi;
import com.bts.bugstalker.util.parameters.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.V1)
public class UserEndpoint implements UserApi {

    @Override
    public ResponseEntity<Void> getUserByPhrase(@Valid String query) {
        return null;
    }


}
