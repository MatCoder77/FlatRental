package com.flatrental.domain.announcement;

import java.text.MessageFormat;
import java.util.Arrays;

public enum AnnouncementType {

    FLAT,
    ROOM,
    PLACE_IN_ROOM;

    private static final String CANNOT_CONVERT_MSG = "Cannot convert from {0} to AnnouncementType";

    public static final AnnouncementType fromString(String type) {
        return Arrays.stream(AnnouncementType.values())
                .filter(announcementType -> announcementType.name().equalsIgnoreCase(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(CANNOT_CONVERT_MSG, type)));
    }

}
