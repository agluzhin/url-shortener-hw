package org.agluzhin.entities;

import java.time.Instant;

public class ShortLink {
    private final String ORIGINAL_URL;
    private final String SHORT_CODE;
    private final String OWNER_ID;
    private final int CLICK_LIMIT;
    private final Instant CREATION_TIME;
    private final long LIFE_LIMIT;

    private int clickCount = 0;

    public ShortLink(String originalURL, String shortCode, String ownerId, int clickLimit, long lifeLimit) {
        ORIGINAL_URL = originalURL;
        SHORT_CODE = shortCode;
        OWNER_ID = ownerId;
        CLICK_LIMIT = clickLimit;
        CREATION_TIME = Instant.now();
        LIFE_LIMIT = lifeLimit;
    }

    public String getOriginalURL() {
        return ORIGINAL_URL;
    }

    public String getShortCode() {
        return SHORT_CODE;
    }

    public String getOwnerId() {
        return OWNER_ID;
    }

    public int getClickLimit() {
        return CLICK_LIMIT;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void incrementClicks() {
        clickCount++;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(CREATION_TIME.plusSeconds(LIFE_LIMIT)) || clickCount >= CLICK_LIMIT;
    }
}
