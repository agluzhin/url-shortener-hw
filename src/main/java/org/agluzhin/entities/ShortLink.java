package org.agluzhin.entities;

import java.time.Instant;

public class ShortLink {
    private final String originalURL;
    private final String shortURL;
    private final String ownerId;
    private final int clickLimit;
    private final Instant creationTime;
    private final long totalSeconds;

    private int clickCount = 0;

    public ShortLink(String originalURL, String shortURL, String ownerId, int clickLimit, long totalSeconds) {
        this.originalURL = originalURL;
        this.shortURL = shortURL;
        this.ownerId = ownerId;
        this.clickLimit = clickLimit;
        this.creationTime = Instant.now();
        this.totalSeconds = totalSeconds;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void incrementClicks() {
        clickCount++;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(creationTime.plusSeconds(totalSeconds)) || clickCount >= clickLimit;
    }
}
