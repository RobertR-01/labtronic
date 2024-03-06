package com.app.labtronic.data;

import java.util.HashMap;
import java.util.Map;

public class ActiveSession {
    private static final ActiveSession ACTIVE_SESSION = new ActiveSession();
    private final Map<Long, CalData> activeCalTabs;
    private static Long LAST_ADDED_ID;

    private ActiveSession() {
        activeCalTabs = new HashMap<>();
    }

    public static ActiveSession getActiveSessionInstance() {
        return ACTIVE_SESSION;
    }

    public void addCalData(CalData calData) {
        LAST_ADDED_ID = calData.getID();
        activeCalTabs.put(LAST_ADDED_ID, calData);
    }

    public HashMap<Long, CalData> getActiveCalTabs() {
        return new HashMap<>(activeCalTabs);
    }

    public static Long getLastAddedId() {
        return LAST_ADDED_ID;
    }
}
