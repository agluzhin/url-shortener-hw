package org.agluzhin.entities;

import java.util.UUID;

public class User {
    private final String id;

    public User() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
}