package com.bts.bugstalker.core.common.exception;

import com.bts.bugstalker.util.generic.BusinessExceptionFactory;

public final class CommonExceptions extends BusinessExceptionFactory {

    private static final String DB_RESULT_NOT_FOUND = "Entity %s with id %s was not found in DB";

    public static class DbQueryResultNotFound extends BusinessException {
        public DbQueryResultNotFound(String entityClassName, String id) {
            super(String.format(DB_RESULT_NOT_FOUND, entityClassName, id));
        }
    }
}
