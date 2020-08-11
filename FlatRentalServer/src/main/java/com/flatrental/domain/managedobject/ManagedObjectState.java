package com.flatrental.domain.managedobject;

import java.util.Set;

public enum ManagedObjectState {

    ACTIVE,
    INACTIVE,
    REMOVED;

    public static Set<ManagedObjectState> getNotRemovedStates() {
        return Set.of(ACTIVE, INACTIVE);
    }

}
