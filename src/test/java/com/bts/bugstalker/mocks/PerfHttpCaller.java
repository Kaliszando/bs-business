package com.bts.bugstalker.mocks;

import com.bts.bugstalker.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RequiredArgsConstructor
@Component
public class PerfHttpCaller {

    private final AuthorizationHeaderMockTool headerMockTool;

    @Setter
    private String path;

    public void call(String path, int expectedStatus, UserRole userRole, String errorCode) {
        given().header(headerMockTool.prepare(userRole))
                .get(path)
                .then().statusCode(expectedStatus)
                .body("code", equalTo(errorCode));
    }

    public void callByTimes(String path, int timesCalled, int expectedStatus, String errorCode) {
        for (int i = 0; i < timesCalled; i++) {
            call(path, expectedStatus, UserRole.ADMIN, errorCode);
        }
    }

    public void callByTimes(String path, int timesCalled, int expectedStatus, String errorCode, UserRole userRole) {
        for (int i = 0; i < timesCalled; i++) {
            call(path, expectedStatus, userRole, errorCode);
        }
    }

    public void call(int expectedStatus, UserRole userRole, String errorCode) {
        validatePath();
        call(path, expectedStatus, userRole, errorCode);
    }

    public void callByTimes(int timesCalled, int expectedStatus, String errorCode) {
        validatePath();
        callByTimes(path, timesCalled, expectedStatus, errorCode);
    }

    private void validatePath() {
        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("url path endpoint is blank");
        }
    }
}
