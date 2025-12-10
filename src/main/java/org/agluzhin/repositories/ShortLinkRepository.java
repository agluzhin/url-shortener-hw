package org.agluzhin.repositories;

import org.agluzhin.entities.ShortLink;

import java.util.HashMap;
import java.util.Map;

public class ShortLinkRepository {
    private final Map<String, Map<String, ShortLink>> LINKS = new HashMap<>();

    public void addLink(String userId, ShortLink link) {
        Map<String, ShortLink> userLinks = LINKS.get(userId);
        userLinks.put(link.getShortUrl(), link);
    }

    public void deleteLink(String userId, String shortUrl) {
        Map<String, ShortLink> userLinks = LINKS.get(userId);
        userLinks.remove(shortUrl);
    }

    public ShortLink getLink(String userId, String shortUrl) {
        Map<String, ShortLink> userLinks = LINKS.get(userId);
        return userLinks.get(shortUrl);
    }

    public Map<String, Map<String, ShortLink>> getAllLinks() {
        return LINKS;
    }
}
