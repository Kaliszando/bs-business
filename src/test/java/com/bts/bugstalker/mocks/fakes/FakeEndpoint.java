package com.bts.bugstalker.mocks.fakes;

import com.bts.bugstalker.core.aop.throttling.ApiThrottle;
import com.bts.bugstalker.core.aop.throttling.model.ThrottlingAlgorithm;
import com.bts.bugstalker.core.aop.throttling.model.ThrottlingScope;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("test")
@RequestMapping("/fake")
public class FakeEndpoint {

    @ApiThrottle(algorithm = ThrottlingAlgorithm.FIXED_WINDOW_COUNTER, limit = 9)
    @GetMapping("/fixed-window-counter")
    public ResponseEntity<FakeResponse> fixedWindowCounter() {
        return ResponseEntity.ok(new FakeResponse("fixed-window-counter"));
    }

    @ApiThrottle(algorithm = ThrottlingAlgorithm.SLIDING_WINDOW_COUNTER, limit = 7)
    @GetMapping("/sliding-window-counter")
    public ResponseEntity<FakeResponse> slidingWindowCounter() {
        return ResponseEntity.ok(new FakeResponse("sliding-window-counter"));
    }

    @ApiThrottle(limit = 3, scope = ThrottlingScope.PER_USER)
    @GetMapping("/limited-per-user")
    public ResponseEntity<FakeResponse> perUser() {
        return ResponseEntity.ok(new FakeResponse("limited-per-user"));
    }

    @ApiThrottle(limit = 3, scope = ThrottlingScope.PER_APP)
    @GetMapping("/limited-per-app")
    public ResponseEntity<FakeResponse> perApp() {
        return ResponseEntity.ok(new FakeResponse("limited-per-app"));
    }
}