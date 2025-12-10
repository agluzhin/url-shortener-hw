package org.agluzhin.entities;

import java.time.Instant;

public class ShortLink {
    private final String ORIGINAL_URL;
    private final String SHORT_URL;
    private final String OWNER_ID;
    private final Instant CREATION_TIME;
    private final int CLICK_LIMIT;
    private final long LIFE_LIMIT;

    private int clickCount = 0;

    public ShortLink(
            String originalUrl,
            String shortUrl,
            String ownerId,
            int clickLimit,
            long lifeLimit
    ) {
        ORIGINAL_URL = originalUrl;
        SHORT_URL = shortUrl;
        OWNER_ID = ownerId;
        CREATION_TIME = Instant.now();
        CLICK_LIMIT = clickLimit;
        LIFE_LIMIT = lifeLimit;
    }

    public String getOriginalURL() {
        return ORIGINAL_URL;
    }

    public String getShortUrl() {
        return SHORT_URL;
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
