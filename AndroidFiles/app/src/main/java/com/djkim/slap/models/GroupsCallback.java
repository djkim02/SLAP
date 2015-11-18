package com.djkim.slap.models;

import java.util.List;

/**
 * Interface for a callback that returns a list of groups.
 */
public interface GroupsCallback {
    void done(List<Group> groups);
}
