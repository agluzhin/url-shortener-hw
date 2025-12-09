package org.agluzhin.entities;

import java.util.UUID;

public class User {
    private final String ID;

    public User() {
        ID = UUID.randomUUID().toString();
    }

    public String getId() {
        return ID;
    }
}