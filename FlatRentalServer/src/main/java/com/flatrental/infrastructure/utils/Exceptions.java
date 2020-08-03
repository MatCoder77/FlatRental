package com.flatrental.infrastructure.utils;

import com.flatrental.domain.managedobject.ManagedObjectState;
import com.flatrental.infrastructure.exceptions.IllegalArgumentAppException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Set;

@UtilityClass
public class Exceptions {

    private static final String OBJECT_NOT_FOUND_MSG = "There is no {0} with id {1} and object state {2}";
    private static final String CANNOT_GET_USER_FROM_CONTEXT =
            "Cannot obtain user from request context. Probable cause: request made by not logged user or user account is not active.";

    public IllegalArgumentAppException getCannotGetUserFromContextException() {
        return new IllegalArgumentAppException(CANNOT_GET_USER_FROM_CONTEXT);
    }

    public IllegalArgumentAppException getObjectNotFoundException(Class<?> objectType, Long id, ManagedObjectState objectState) {
        return new IllegalArgumentAppException(MessageFormat.format(OBJECT_NOT_FOUND_MSG, objectType.getSimpleName(), String.valueOf(id), objectState));
    }

    public IllegalArgumentAppException getObjectNotFoundException(Class<?> objectType, Long id, Set<ManagedObjectState> objectStates) {
        return new IllegalArgumentAppException(MessageFormat.format(OBJECT_NOT_FOUND_MSG, objectType.getSimpleName(), String.valueOf(id), StringUtils.join(objectStates, " or ")));
    }

}
